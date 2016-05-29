package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.effects.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matt on 5/24/2016.
 */
public class EntityCallbackContainer extends BaseEventMatch<Entity, IEntityMatcherData, IEntityMatcher> implements IEntityCallbackContainer {
    private final List<IEntityCallback> callbacks = new ArrayList<>();

    private void addCallback(@Nullable IEntityCallback callback) {
        if(callback != null) callbacks.add(callback);
    }

    @Override
    public void apply(Entity source) {
        // Figure out if we match
        BaseMatchResult result = matches(source, new BaseEntityMatcherData());

        // If we didn't match, check for alt matches...
        if(!result.isMatch()) {
            if (getAltMatch() == null) return;

            // Run them if so:
            ((EntityCallbackContainer)getAltMatch()).apply(source);
            return;
        }

        // Run our matcher callbacks (if they exist...)
        result.runIfMatch();

        // Run our callbacks
        for(IEntityCallback callback : callbacks) {
            callback.apply(source);
        }
    }

    @ScriptObjectDeserializer(deserializes = IEntityCallbackContainer.class)
    public static class Deserializer implements IScriptObjectDeserializer {
        private static final BaseEntityCallback.Deserializer CALLBACK_DESERIALIZER = new BaseEntityCallback.Deserializer();

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
                    LogHelper.error("Unable to convert given callback function into IEntityCallbackContainer", e);
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

                // Just deserialize the callback itself...
                container.addCallback(DeserializerHelpers.convertWithDeserializer(mirror, CALLBACK_DESERIALIZER, IEntityCallback.class));
                return container;
            }

            // Add them to the container...
            IEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "then", CALLBACK_DESERIALIZER, IEntityCallback.class);
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


            // If we have an alt match...
            if (!mirror.containsKey("otherwise")) return container;

            Object altMatchData = mirror.get("otherwise");

            // Try to deserialize it:
            try {
                container.setAltMatch((EntityCallbackContainer) Deserialize(altMatchData));
            } catch (Exception ex) {
                LogHelper.warn("Unable to deserialize 'otherwise' for this object.");
            }

            return container;
        }
    }

    public static class Handler extends BaseEventMatch.BaseMatchHandler<IEntityCallbackContainer> {
        @Override
        public void apply(Random rand, EntityLivingBase entity) {
            // Easy enough... just apply all the callback containers we have:
            for(IEntityCallbackContainer callbackContainer : matchers) {
                callbackContainer.apply(entity); // Because apply does check matchers as well.
            }
        }
    }
}
