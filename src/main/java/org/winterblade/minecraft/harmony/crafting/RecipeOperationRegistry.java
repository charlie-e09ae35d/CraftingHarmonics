package org.winterblade.minecraft.harmony.crafting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeOperationRegistry {
    private final static Map<String, Class<BaseRecipeOperation>> deserializerMap = new TreeMap<>();

    public static void CreateDeserializers(Map<String, Class<BaseRecipeOperation>> deserializers) {
        for(Map.Entry<String, Class<BaseRecipeOperation>> deserializer : deserializers.entrySet()) {
            RecipeOperation anno = deserializer.getValue().getAnnotation(RecipeOperation.class);

            // Check if we have the specified mod loaded:
            if(!anno.dependsOn().equals("") && !Loader.isModLoaded(anno.dependsOn())) {
                LogHelper.warn(anno.name() + " depends on '" + anno.dependsOn() + "', which is not loaded.");
                continue;
            } else {
                LogHelper.info("Registering operation '" + anno.name() + "'.");
            }

            deserializerMap.put(deserializer.getKey().toLowerCase(), deserializer.getValue());
        }
    }

    /**
     * Called from our internal scripts in order to create the operation.
     * @param setName      The name of the set to add it to.
     * @param type      The type of the operation
     * @param operation A script object
     * @return          True if the operation processed fine; false otherwise.
     */
    public static boolean CreateOperationInSet(String setName, String type, ScriptObjectMirror operation) {
        type = type.toLowerCase();
        if(!deserializerMap.containsKey(type)) {
            LogHelper.warn("Unknown recipe operation type '" + type + "' for set '" + setName + "'.  Are you missing an addon?");
            return false;
        }

        Class source = deserializerMap.get(type);
        BaseRecipeOperation inst;
        try {
            inst = (BaseRecipeOperation)source.newInstance();
        } catch (Exception e) {
            LogHelper.error("Unable to create instance of " + source.getCanonicalName(),e);
            return false;
        }
        if(!inst.Convert(NashornConfigProcessor.getInstance().nashorn,operation)) return false;

        CraftingHarmonicsMod.AddOperationToSet(setName, inst);

        return true;
    }
}
