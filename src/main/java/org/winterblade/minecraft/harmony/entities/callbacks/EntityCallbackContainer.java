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
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.effects.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.utility.BasePrioritizedData;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/24/2016.
 */
public class EntityCallbackContainer implements IEntityCallback {
    private final PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers = new PriorityQueue<>();
    private final List<IEntityCallback> callbacks = new ArrayList<>();

    private void addMatcher(IEntityMatcher matcher, Priority priority) {
        matchers.add(new BasePrioritizedData<>(matcher, priority));
    }

    private void addCallback(@Nullable IEntityCallback callback) {
        if(callback != null) callbacks.add(callback);
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

        // Run our callbacks
        for(IEntityCallback callback : callbacks) {
            callback.apply(source);
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

            // Make sure we have callbacks:
            if(!mirror.containsKey("then")) {
                LogHelper.warn("Callback set contains no callbacks.");
                return null;
            }

            // Add them to the container...
            IEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "then", null, IEntityCallback.class);
            for(IEntityCallback callback : callbacks) {
                container.addCallback(callback);
            }

            // Get our registry data...
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                    IEntityMatcher.class}, mirror);

            // And find our matchers...
            List<IEntityMatcher> matchers = registry.getComponentsOf(IEntityMatcher.class);

            for(IEntityMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                container.addMatcher(matcher, priority);
            }

            return container;
        }
    }
}
