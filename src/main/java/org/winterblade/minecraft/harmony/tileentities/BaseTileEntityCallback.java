package org.winterblade.minecraft.harmony.tileentities;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.tileentities.callbacks.FunctionCallback;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.*;

/**
 * Created by Matt on 5/29/2016.
 */
public class BaseTileEntityCallback extends BaseEventMatch<TileEntity, CallbackMetadata, ITileEntityMatcher> implements ITileEntityCallback {
    private static final Map<String, Class<BaseTileEntityCallback>> callbackMap = new HashMap<>();
    private final List<ITileEntityCallback> callbacks = new ArrayList<>();

    /**
     * Add the classes that we should register for entity callbacks.
     * @param callbackClasses    A map of the callback names to their respective classes.
     */
    public static void registerCallbacks(Map<String, Class<BaseTileEntityCallback>> callbackClasses) {
        for(Map.Entry<String, Class<BaseTileEntityCallback>> entry : callbackClasses.entrySet()) {
            TileEntityCallback anno = entry.getValue().getAnnotation(TileEntityCallback.class);

            if(!anno.dependsOn().equals("") && !Loader.isModLoaded(anno.dependsOn())) {
                LogHelper.warn("TileEntity callback '{}' depends on '{}', which is not loaded.", anno.name(), anno.dependsOn());
                continue;
            }

            LogHelper.info("Registering TileEntity callback '{}'.", anno.name());
            callbackMap.put(anno.name().toLowerCase(), entry.getValue());
        }
    }

    @Override
    public final void apply(TileEntity target, CallbackMetadata metadata) {
        // Figure out if we match
        Data data = new Data();
        BaseMatchResult result = matches(target, data);

        // If we didn't match, check for alt matches...
        if(!result.isMatch()) {
            if (getAltMatch() == null) return;

            // Run them if so:
            ((BaseTileEntityCallback)getAltMatch()).apply(target, metadata);
            return;
        }

        // Run our matcher callbacks (if they exist...)
        result.runIfMatch();

        // Finally, actually do our thing...
        applyTo(target, data);
    }

    /**
     * Apply an action to the target.
     * @param target    The target to apply to.
     * @param data      Any event data to deal with.
     */
    protected void applyTo(TileEntity target, CallbackMetadata data) {
        // Run our callbacks
        for(ITileEntityCallback callback : callbacks) {
            callback.apply(target, data);
        }
    }

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     * @param mirror    The mirror to update from
     */
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        // Does nothing.
    }

    /**
     * Called to run callbacks on the given target
     * @param callbacks    The callbacks to run
     * @param target       The target to run them on.
     */
    protected void runCallbacks(ITileEntityCallback[] callbacks, TileEntity target) {
        if(callbacks == null) return;

        TileEntityTickRegistry.addCallbackSet(target, callbacks);
    }

    /**
     * Add in our callbacks
     * @param callback    The callback to add.
     */
    private void addCallback(ITileEntityCallback callback) {
        if(callback != null) callbacks.add(callback);
    }

    /**
     * Container for holding our callback data.
     */
    protected static class Data extends CallbackMetadata {

    }

    @ScriptObjectDeserializer(deserializes = ITileEntityCallback.class)
    public static class Deserializer implements IScriptObjectDeserializer {
        @Override
        public final Object Deserialize(Object input) {
            // Method callback
            if(ScriptFunction.class.isAssignableFrom(input.getClass())) {
                try {
                    // Wrap it so we can pass interops instead of base objects
                    FunctionCallback fn = new FunctionCallback((FunctionCallback.JSCallback) ScriptUtils.convert(input, FunctionCallback.JSCallback.class));
                    BaseTileEntityCallback container = new BaseTileEntityCallback();
                    container.addCallback(fn);
                    return container;
                } catch (Exception e) {
                    LogHelper.error("Unable to convert given callback function into ITileEntityCallback", e);
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

            BaseTileEntityCallback container = new BaseTileEntityCallback();

            // Add them to the container...
            ITileEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "then", this, ITileEntityCallback.class);
            for(ITileEntityCallback callback : callbacks) {
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
                LogHelper.warn("Unable to deserialize 'otherwise' for this TileEntity callback.");
            }
        }

        /**
         * Get the matchers on the mirror
         * @param mirror    The mirror to check
         * @return          A list of matchers
         */
        private List<ITileEntityMatcher> getMatchers(ScriptObjectMirror mirror) {
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                    ITileEntityMatcher.class}, mirror);

            // And find our matchers...
            return registry.getComponentsOf(ITileEntityMatcher.class);
        }

        /**
         * Deserialize our callback classes.
         *
         * @param type      The type to deserialize
         * @param mirror    The mirror to check
         * @return          Our deserialized callback
         */
        private BaseTileEntityCallback deserializeCallback(String type, ScriptObjectMirror mirror) {
            BaseTileEntityCallback output = newTypedInstance(type);
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
        private BaseTileEntityCallback newTypedInstance(String type) {
            type = type.toLowerCase();

            if(!callbackMap.containsKey(type)) {
                LogHelper.error("Error creating TileEntity callback of type '{}': The type is not registered.", type);
                return null;
            }

            try {
                return callbackMap.get(type).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LogHelper.error("Error creating TileEntity callback of type '{}'.", type, e);
                return null;
            }
        }
    }

    public static class Handler extends BaseMatchHandler<ITileEntityCallback,TileEntity> {
        @Override
        public void apply(Random rand, TileEntity entity) {
            for(ITileEntityCallback callback : matchers) {
                callback.apply(entity, new Data());
            }
        }
    }
}
