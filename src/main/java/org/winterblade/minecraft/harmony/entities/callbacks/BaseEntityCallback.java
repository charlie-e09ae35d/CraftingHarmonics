package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.common.utility.BasePrioritizedData;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Matt on 5/26/2016.
 */
public class BaseEntityCallback implements IEntityCallback {
    private static final Map<String, Class<BaseEntityCallback>> callbackMap = new HashMap<>();
    private final PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers = new PriorityQueue<>();
    private final List<IEntityCallback> callbacks = new ArrayList<>();
    private final List<IEntityTargetModifier> targetModifiers = new ArrayList<>();

    // Serialized property for every callback; only used by some.
    protected String id;
    private BaseEntityCallback altMatch;

    private void addCallback(@Nullable IEntityCallback callback) {
        if(callback != null) callbacks.add(callback);
    }

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
    public final void apply(Entity target, CallbackMetadata data) {
        // Figure out if we match
        BaseMatchResult result = BaseEntityMatcherData.match(target, matchers);
        if(!result.isMatch()) {
            if (altMatch == null) return;

            // Run them if so:
            altMatch.apply(target, data);
            return;
        }

        // Run our matcher callbacks (if they exist...)
        result.runIfMatch();

        Set<Entity> targets = new HashSet<>();
        targets.add(target);
        for(IEntityTargetModifier modifier : targetModifiers) {
            targets.addAll(modifier.getTargets(target, data));
        }

        for(Entity subtarget : targets) {
            if(subtarget == null) continue;
            applyTo(subtarget, data);
        }
    }

    protected void applyTo(Entity target, CallbackMetadata metadata) {
        // Run our callbacks
        for(IEntityCallback callback : callbacks) {
            callback.apply(target, metadata);
        }
    }

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

    protected void runCallbacks(IEntityCallback[] callbacks, Entity target) {
        if(callbacks == null) return;

        MobTickRegistry.addCallbackSet(target, callbacks);
    }

    private void setAltMatch(BaseEntityCallback altMatch) {
        this.altMatch = altMatch;
    }

    @ScriptObjectDeserializer(deserializes = IEntityCallback.class)
    public static class Deserializer implements IScriptObjectDeserializer {
        @Override
        public final Object Deserialize(Object input) {
            // Method callback
            if(ScriptFunction.class.isAssignableFrom(input.getClass())) {
                try {
                    // Wrap it so we can pass interops instead of base objects
                    FunctionCallback fn = new FunctionCallback((FunctionCallback.JSCallback) ScriptUtils.convert(input, FunctionCallback.JSCallback.class));
                    BaseEntityCallback container = new BaseEntityCallback();
                    container.addCallback(fn);
                    return container;
                } catch (Exception e) {
                    LogHelper.error("Unable to convert given callback function into IEntityCallback", e);
                    return null;
                }
            }

            // Make sure we can continue:
            if(!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                    !ScriptObject.class.isAssignableFrom(input.getClass())) return null;

            ScriptObjectMirror mirror;

            // The first case will probably not happen, but, just in case...
            if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
                mirror = (ScriptObjectMirror) input;
            } else {
                mirror = ScriptUtils.wrap((ScriptObject) input);
            }

            // Make sure we have callbacks:
            if(!mirror.containsKey("then")) {
                // Check if this is a single callback with no matchers...
                if(!mirror.containsKey("type")) {
                    LogHelper.warn("Callback set contains no callbacks.");
                    return null;
                }

                return deserializeCallback(mirror.get("type").toString(), mirror);
            }

            BaseEntityCallback container = new BaseEntityCallback();

            // Add them to the container...
            IEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "then", this, IEntityCallback.class);
            for(IEntityCallback callback : callbacks) {
                container.addCallback(callback);
            }

            registerMatchersAndOtherwise(mirror, container);

            return container;
        }

        /**
         * Register our matchers and 'otherwise' object (if any)
         * @param mirror       The mirror to add things from
         * @param container    The container to add them to.
         */
        private void registerMatchersAndOtherwise(ScriptObjectMirror mirror, BaseEntityCallback container) {
            // Get our registry data...
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                    IEntityMatcher.class, IEntityTargetModifier.class}, mirror);

            // And find our matchers...
            List<IEntityMatcher> matchers = registry.getComponentsOf(IEntityMatcher.class);

            for(IEntityMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                container.addMatcher(matcher, priority);
            }

            // And our modifiers...
            List<IEntityTargetModifier> modifiers = registry.getComponentsOf(IEntityTargetModifier.class);
            for(IEntityTargetModifier modifier : modifiers) {
                container.targetModifiers.add(modifier);
            }

            // If we have an alt match...
            if (!mirror.containsKey("otherwise")) return;

            Object altMatchData = mirror.get("otherwise");

            // Try to deserialize it:
            try {
                container.setAltMatch((BaseEntityCallback) Deserialize(altMatchData));
            } catch (Exception ex) {
                LogHelper.warn("Unable to deserialize 'otherwise' for this Entity callback.");
            }
        }

        /**
         * Deserialize our callback classes.
         *
         * @param type      The type to deserialize
         * @param mirror    The mirror to check
         * @return          Our deserialized callback
         */
        private BaseEntityCallback deserializeCallback(String type, ScriptObjectMirror mirror) {
            BaseEntityCallback output = newTypedInstance(type);
            if(output == null) return null;

            NashornConfigProcessor.getInstance().nashorn.parseScriptObject(mirror, output);
            output.finishDeserialization(mirror);

            registerMatchersAndOtherwise(mirror, output);

            return output;
        }

        /**
         * Create an instance of our type
         * @param type    The type to create
         * @return        The new instance, ready to be populated.
         */
        private BaseEntityCallback newTypedInstance(String type) {
            type = type.toLowerCase();

            if(!callbackMap.containsKey(type)) {
                LogHelper.error("Error creating Entity callback of type '{}': The type is not registered.", type);
                return null;
            }

            try {
                return callbackMap.get(type).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LogHelper.error("Error creating Entity callback of type '{}'.", type, e);
                return null;
            }
        }
    }

    public static class Handler extends BaseEventMatch.BaseMatchHandler<IEntityCallback, EntityLivingBase> {
        @Override
        public void apply(Random rand, EntityLivingBase entity) {
            // Easy enough... just apply all the callback containers we have:
            for(IEntityCallback callbackContainer : matchers) {
                callbackContainer.apply(entity, new BaseEntityMatcherData(entity)); // Because apply does check matchers as well.
            }
        }
    }
}
