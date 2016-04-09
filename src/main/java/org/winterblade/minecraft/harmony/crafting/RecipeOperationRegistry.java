package org.winterblade.minecraft.harmony.crafting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeOperationRegistry {
    private final static Map<String, Class<BaseRecipeOperation>> deserializerMap = new TreeMap<>();

    public static void CreateDeserializers(Map<String, Class<BaseRecipeOperation>> deserializers) {
        for(Map.Entry<String, Class<BaseRecipeOperation>> deserializer : deserializers.entrySet()) {
            deserializerMap.put(deserializer.getKey(), deserializer.getValue());
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
            System.err.println("Unknown recipe operation type '" + type + "' for set '" + setName + "'.  Are you missing an addon?");
            return false;
        }

        Class source = deserializerMap.get(type);
        BaseRecipeOperation inst;
        try {
            inst = (BaseRecipeOperation)source.newInstance();
        } catch (Exception e) {
            System.out.println("Unable to create instance of " + source.getCanonicalName() + ": " + e.getMessage());
            return false;
        }
        if(!inst.Convert(operation)) return false;

        CraftingHarmonicsMod.AddOperationToSet(setName, inst);

        return true;
    }
}
