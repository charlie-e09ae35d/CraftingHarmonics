package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.effects.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.utility.BasePrioritizedData;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.*;

/**
 * Created by Matt on 5/24/2016.
 */
public class EntityCallbackContainer implements IEntityCallback {
    private final PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers = new PriorityQueue<>();
    private final PriorityQueue<BasePrioritizedData<IEntityTargetModifier>> modifiers = new PriorityQueue<>();
    private final List<IEntityCallback> callbacks = new ArrayList<>();

    private void addMatcher(IEntityMatcher matcher, Priority priority) {
        matchers.add(new BasePrioritizedData<>(matcher, priority));
    }

    private void addTargetModifier(IEntityTargetModifier modifier, Priority priority) {
        modifiers.add(new BasePrioritizedData<>(modifier, priority));
    }

    private void addCallback(IEntityCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void apply(Entity source) {
        List<Runnable> matcherCallbacks = new ArrayList<>();

        // Match us...
        IEntityMatcherData metadata = new BaseEntityMatcherData();
        for(BasePrioritizedData<IEntityMatcher> matcher : matchers) {
            BaseMatchResult matchResult = matcher.get().isMatch(source, metadata);
            if(!matchResult.isMatch()) return;
            if(matchResult.getCallback() != null) matcherCallbacks.add(matchResult.getCallback());
        }

        // Run our matcher callbacks...
        matcherCallbacks.forEach(Runnable::run);

        // If we're not modified, just run it on the target...
        if(modifiers.size() <= 0) {
            runOn(source);
            return;
        }

        Set<Entity> targets = new HashSet<>();

        for(BasePrioritizedData<IEntityTargetModifier> modifier : modifiers) {
            targets.addAll(modifier.get().getTargets(source));
        }

        for(Entity target : targets) {
            runOn(target);
        }
    }

    private void runOn(Entity target) {
        // Run the actual callbacks...
        for(IEntityCallback callback : callbacks) {
            callback.apply(target);
        }
    }

    @ScriptObjectDeserializer(deserializes = IEntityCallback.class)
    public static class Deserializer implements IScriptObjectDeserializer {
        @Override
        public Object Deserialize(Object input) {
            EntityCallbackContainer container = new EntityCallbackContainer();

            // Method callback
            if(ScriptFunction.class.isAssignableFrom(input.getClass())) {
                try {
                    // Wrap it so we can pass interops instead of base objects
                    FunctionCallback fn = new FunctionCallback((FunctionCallback.JSCallback) ScriptUtils.convert(input, FunctionCallback.JSCallback.class));
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

            // Get our registry data...
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                    IEntityCallback.class,
                    IEntityMatcher.class,
                    IEntityTargetModifier.class}, mirror);

            List<IEntityMatcher> matchers = registry.getComponentsOf(IEntityMatcher.class);
            List<IEntityCallback> callbacks = registry.getComponentsOf(IEntityCallback.class);
            List<IEntityTargetModifier> modifiers = registry.getComponentsOf(IEntityTargetModifier.class);

            for (IEntityCallback callback : callbacks) {
                container.addCallback(callback);
            }

            for(IEntityMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                container.addMatcher(matcher, priority);
            }

            for(IEntityTargetModifier modifier : modifiers) {
                PrioritizedObject priorityAnno = modifier.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                container.addTargetModifier(modifier, priority);
            }

            return container;
        }
    }
}
