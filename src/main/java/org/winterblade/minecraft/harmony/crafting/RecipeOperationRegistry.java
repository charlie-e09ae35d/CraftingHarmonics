package org.winterblade.minecraft.harmony.crafting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Matt on 4/8/2016.
 */
public class RecipeOperationRegistry {
    private final static Map<String, Class<BasicOperation>> deserializerMap = new TreeMap<>();

    public static void CreateDeserializers(Map<String, Class<BasicOperation>> deserializers) {
        for(Map.Entry<String, Class<BasicOperation>> deserializer : deserializers.entrySet()) {
            Operation anno = deserializer.getValue().getAnnotation(Operation.class);

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

    public static BasicOperation createOperation(String type, ScriptObjectMirror operation) {
        type = type.toLowerCase();
        if(!deserializerMap.containsKey(type)) {
            return null;
        }

        Class source = deserializerMap.get(type);
        BasicOperation inst;
        try {
            inst = (BasicOperation)source.newInstance();
        } catch (Exception e) {
            return null;
        }

        if(!inst.convert(NashornConfigProcessor.getInstance().nashorn,operation)) return null;

        return inst;
    }

    /**
     * Called from our internal scripts in order to create the operation.
     * @param setName      The name of the set to add it to.
     * @param type      The type of the operation
     * @param operation A script object
     * @return          True if the operation processed fine; false otherwise.
     */
    public static boolean CreateOperationInSet(String setName, String type, ScriptObjectMirror operation) {
        BasicOperation inst = createOperation(type, operation);
        if(inst == null) {
            LogHelper.warn("Unknown recipe operation type '" + type + "' for set '" + setName + "'.  Are you missing an addon?");
            return false;
        }

        CraftingHarmonicsMod.AddOperationToSet(setName, inst);

        return true;
    }
}
