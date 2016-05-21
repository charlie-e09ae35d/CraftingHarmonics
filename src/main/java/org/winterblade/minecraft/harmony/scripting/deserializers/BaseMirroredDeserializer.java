package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

import java.lang.reflect.Array;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseMirroredDeserializer implements IScriptObjectDeserializer {
    @Override
    public final Object Deserialize(Object input) {
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

        return DeserializeMirror(mirror);
    }

    protected abstract Object DeserializeMirror(ScriptObjectMirror mirror);

    @SuppressWarnings("unechecked")
    protected <T> T[] convertArrayWithDeserializer(ScriptObjectMirror mirror, String key, IScriptObjectDeserializer deserializer, Class<T> toClass) {
        try {
            Object[] items = (Object[]) ScriptUtils.convert(mirror.get(key), Object[].class);
            T[] output = (T[]) Array.newInstance(toClass, items != null ? items.length : 0);

            for (int i = 0; i < output.length; i++) {
                try {
                    output[i] = (T) deserializer.Deserialize(items[i]);
                } catch (Exception e) {
                    LogHelper.error("Error deserializing element of " + toClass.getName(), e);
                    output[i] = null;
                }
            }

            return output;
        } catch (Exception e) {
            LogHelper.error("Error deserializing array of " + toClass.getName(), e);
            return null;
        }
    }
}
