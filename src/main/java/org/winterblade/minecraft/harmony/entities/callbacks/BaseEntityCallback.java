package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.effects.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseComponentDeserializer;
import org.winterblade.minecraft.harmony.utility.BasePrioritizedData;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class BaseEntityCallback implements IEntityCallback {
    private static final Map<String, Class<BaseEntityCallback>> callbackMap = new HashMap<>();
    private final PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers = new PriorityQueue<>();

    /**
     * Add the classes that we should register for entity callbacks.
     * @param callbackClasses    A map of the callback names to their respective classes.
     */
    public static void registerCallbacks(Map<String, Class<BaseEntityCallback>> callbackClasses) {
        for(Map.Entry<String, Class<BaseEntityCallback>> entry : callbackClasses.entrySet()) {
            EntityCallback anno = entry.getValue().getAnnotation(EntityCallback.class);

            if(!anno.dependsOn().equals("") && !Loader.isModLoaded(anno.dependsOn())) {
                LogHelper.warn("Entity callback '{}' depends on '{}', which is not loaded.", anno.name(), anno.dependsOn());
                continue;
            }

            LogHelper.info("Registering entity callback '{}'.", anno.name());
            callbackMap.put(anno.name().toLowerCase(), entry.getValue());
        }
    }

    @Override
    public void apply(Entity target) {
        // Figure out if we match
        BaseMatchResult result = BaseEntityMatcherData.match(target, matchers);
        if(!result.isMatch()) return;

        // Run our matcher callbacks (if they exist...)
        result.runIfMatch();

        // TODO: Target mod...

        applyTo(target);
    }

    protected abstract void applyTo(Entity target);

    /**
     * Adds a matcher to this callback
     * @param matcher   The matcher to add.
     * @param priority  The priority to add the matcher at.
     */
    private void addMatcher(IEntityMatcher matcher, Priority priority) {
        if(matcher == null) return;
        if(priority == null) priority = Priority.MEDIUM;
        matchers.add(new BasePrioritizedData<>(matcher, priority));
    }

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     * @param mirror    The mirror to update from
     */
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        // Does nothing.
    }

    protected void runCallbacks(IEntityCallbackContainer[] callbacks, Entity target) {
        if(callbacks == null) return;

        MobTickRegistry.addCallbackSet(target, callbacks);
    }

    @ScriptObjectDeserializer(deserializes = BaseEntityCallback.class)
    public static class Deserializer extends BaseComponentDeserializer<BaseEntityCallback, IEntityMatcher> {
        public Deserializer() {
            super(IEntityMatcher.class);
        }

        @Override
        protected BaseEntityCallback newInstance(String type) {
            type = type.toLowerCase();

            if(!callbackMap.containsKey(type)) {
                LogHelper.error("Error creating entity callback of type '{}': The type is not registered.", type);
                return null;
            }
            try {
                return (BaseEntityCallback) callbackMap.get(type).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LogHelper.error("Error creating entity callback of type '{}'.", type, e);
                return null;
            }
        }

        @Override
        protected void update(ScriptObjectMirror mirror, BaseEntityCallback output, List<IEntityMatcher> matchers) {
            for(IEntityMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                output.addMatcher(matcher, priority);
            }

            // Deserialize the rest with the default serializer
            NashornConfigProcessor.getInstance().nashorn.parseScriptObject(mirror, output);
            output.finishDeserialization(mirror);
        }
    }
}
