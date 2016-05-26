package org.winterblade.minecraft.harmony.scripting;

import com.google.common.collect.ObjectArrays;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

import javax.annotation.Nullable;

/**
 * Collection of utilities that need to be ported into NashornLib eventually.
 */
public class DeserializerHelpers {
    public static <T> T[] convertArrayWithDeserializer(ScriptObjectMirror mirror, String key, IScriptObjectDeserializer deserializer, Class<T> toClass) {
        try {
            Object val = mirror.get(key);
            T[] output;

            // If we don't anything to deserialize...
            if(val == null) {
                return ObjectArrays.newArray(toClass, 0);
            }

            if(ScriptObjectMirror.class.isAssignableFrom(val.getClass())) {
                ScriptObjectMirror valMirror = (ScriptObjectMirror) val;

                // If we have an array, convert the array...
                if(valMirror.isArray()) {
                    Object[] items = (Object[]) ScriptUtils.convert(val, Object[].class);
                    output = ObjectArrays.newArray(toClass, items != null ? items.length : 0);

                    if (items == null) return output;

                    for (int i = 0; i < output.length; i++) {
                        output[i] = convertWithDeserializer(items[i], deserializer, toClass);
                    }
                // Otherwise, just convert the object, and turn it into an array:
                } else {
                    output = ObjectArrays.newArray(toClass, 1);
                    output[0] = convertWithDeserializer(valMirror, deserializer, toClass);
                }

                return output;
            } else {
                output = ObjectArrays.newArray(toClass, 1);
                output[0] = convertWithDeserializer(val, deserializer, toClass);
            }

            return output;
        } catch (Exception e) {
            LogHelper.error("Error deserializing array of " + toClass.getName(), e);
            return ObjectArrays.newArray(toClass, 0);
        }
    }

    /**
     * Conver the object to the given type via the provided deserializer
     * @param input           The object to convert
     * @param deserializer    The deserializer to use
     * @param toClass         The class to convert to
     * @param <T>             The class to convert to
     * @return                The converted class; null if it couldn't be converted to the given type.
     */
    @Nullable
    public static <T> T convertWithDeserializer(Object input, IScriptObjectDeserializer deserializer, Class<T> toClass) {
        if(input == null) return null;
        try {
            Object out = deserializer.Deserialize(input);
            if(out == null) return null;
            return toClass.isAssignableFrom(out.getClass()) ? toClass.cast(out) : null;
        } catch (Exception e) {
            LogHelper.error("Error deserializing element of " + toClass.getName(), e);
            return null;
        }
    }
}
