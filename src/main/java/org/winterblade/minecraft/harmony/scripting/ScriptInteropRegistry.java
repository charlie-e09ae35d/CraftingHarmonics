package org.winterblade.minecraft.harmony.scripting;

import com.google.common.collect.ImmutableMap;
import org.winterblade.minecraft.harmony.api.ScriptInterop;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/22/2016.
 */
public class ScriptInteropRegistry {
    private static Map<String, String[]> interops = new HashMap<>();

    private ScriptInteropRegistry() {}

    public static void registerInterops(Map<String, Class<Object>> interopClasses) {
        for(Map.Entry<String, Class<Object>> entry : interopClasses.entrySet()) {
            Class interopClass = entry.getValue();

            try {
                ScriptInterop annotation = (ScriptInterop) interopClass.getAnnotation(ScriptInterop.class);

                String wrappedClass = annotation.wraps() != Object.class
                        ? annotation.wraps().getCanonicalName()
                        : interopClass.getCanonicalName();

                interops.put(interopClass.getCanonicalName(), wrappedClass.split("\\."));
            } catch(Exception e) {
                LogHelper.info("Error registering interop class: '" + interopClass.getName() + "'.", e);
            }
        }
    }

    public static Map<String, String[]> getInterops() {
        return ImmutableMap.copyOf(interops);
    }
}
