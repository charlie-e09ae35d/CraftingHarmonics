package org.winterblade.minecraft.harmony.tileentities;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.*;

/**
 * Created by Matt on 5/29/2016.
 */
public class BaseTileEntityCallback extends BaseEventMatch<TileEntity, ITileEntityMatcherData, ITileEntityMatcher> implements ITileEntityCallback {
    private static final Map<String, Class<ITileEntityCallback>> callbackMap = new HashMap<>();
    private final List<ITileEntityCallback> callbacks = new ArrayList<>();

    @Override
    public void apply(TileEntity target) {

    }

    private void addCallback(ITileEntityCallback callback) {
        if(callback != null) callbacks.add(callback);
    }


    @ScriptObjectDeserializer(deserializes = ITileEntityCallback.class)
    public static class Deserializer implements IScriptObjectDeserializer {
        @Override
        public Object Deserialize(Object input) {
            // Method callback
            // TODO: This.
//            if(ScriptFunction.class.isAssignableFrom(input.getClass())) {
//                try {
//                    // Wrap it so we can pass interops instead of base objects
//                    FunctionCallback fn = new FunctionCallback((FunctionCallback.JSCallback) ScriptUtils.convert(input, FunctionCallback.JSCallback.class));
//                    container.addCallback(fn);
//                    return container;
//                } catch (Exception e) {
//                    LogHelper.error("Unable to convert given callback function into IEntityCallbackContainer", e);
//                    return null;
//                }
//            }

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

                return deserializeCallback(mirror);
            }

            BaseTileEntityCallback container = new BaseTileEntityCallback();

            // Add them to the container...
            ITileEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "then", this, ITileEntityCallback.class);
            for(ITileEntityCallback callback : callbacks) {
                container.addCallback(callback);
            }

            registerMatchersAndOtherwise(mirror, container);

            return container;
        }

        private void registerMatchersAndOtherwise(ScriptObjectMirror mirror, BaseTileEntityCallback container) {
            // Get our registry data...
            List<ITileEntityMatcher> matchers = getMatchers(mirror);

            for(ITileEntityMatcher matcher : matchers) {
                PrioritizedObject priorityAnno = matcher.getClass().getAnnotation(PrioritizedObject.class);
                Priority priority = priorityAnno != null ? priorityAnno.priority() : Priority.MEDIUM;
                container.addMatcher(matcher, priority);
            }


            // If we have an alt match...
            if (!mirror.containsKey("otherwise")) return;

            Object altMatchData = mirror.get("otherwise");

            // Try to deserialize it:
            try {
                container.setAltMatch((BaseTileEntityCallback) Deserialize(altMatchData));
            } catch (Exception ex) {
                LogHelper.warn("Unable to deserialize 'otherwise' for this object.");
            }
        }

        private List<ITileEntityMatcher> getMatchers(ScriptObjectMirror mirror) {
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                    ITileEntityMatcher.class}, mirror);

            // And find our matchers...
            return registry.getComponentsOf(ITileEntityMatcher.class);
        }

        private ITileEntityCallback deserializeCallback(ScriptObjectMirror mirror) {
            return null;
        }
    }

    public static class Handler extends BaseMatchHandler<BaseTileEntityCallback,TileEntity> {
        @Override
        public void apply(Random rand, TileEntity entity) {
            LogHelper.info("Applying event for {}", entity.getBlockType().getLocalizedName());
        }
    }
}
