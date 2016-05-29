package org.winterblade.minecraft.harmony;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.*;

/**
 * Created by Matt on 4/8/2016.
 */
public class SetManager {
    private final static Map<String, Long> setsOnCooldown = new HashMap<>();
    private final static Map<String, Long> setsToExpire = new HashMap<>();
    public static final String SETS_TO_EXPIRE_TAG_NAME = "SetsToExpire";
    public static final String SETS_ON_COOLDOWN_TAG_NAME = "SetsOnCooldown";
    private static long lastTickTime;

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
     * Called from our internal scripts in order to create a set
     */
    public static OperationSet registerSet(String setName) {
        return CraftingHarmonicsMod.getOrCreateSet(setName);
    }

    /**
     * Called by the server tick to see if we need to remove sets.
     */
    static boolean update() {
        lastTickTime = DimensionManager.getWorld(0).getTotalWorldTime();

        boolean updatedConfigs = false;

        // Check our currently pending sets:
        for (Iterator<Map.Entry<String, Long>> iterator = setsToExpire.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Long> entry = iterator.next();
            if (lastTickTime < entry.getValue()) continue;

            updatedConfigs = true;

            iterator.remove();
            LogHelper.info("Set {}'s duration has expired.", entry.getKey());
            CraftingHarmonicsMod.undoSet(entry.getKey());
        }

        // Remove any sets that no longer need to be on cooldown
        for (Iterator<Map.Entry<String, Long>> iterator = setsOnCooldown.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry<String, Long> entry = iterator.next();
            if (lastTickTime < entry.getValue()) continue;
            LogHelper.info("Set {} is no longer on cooldown.", entry.getKey());
            iterator.remove();
        }

        return updatedConfigs;
    }

    /**
     * Called when a set with a duration is applied
     * @param setName     The set name
     * @param duration    The duration
     */
    static void setWithDurationApplied(String setName, int duration) {
        // If we haven't run our first update tick: don't care.
        if(lastTickTime <= 0) return;

        // Otherwise, go ahead and say that we need to expire at some point
        LogHelper.info("Set {} will expire in {} ticks.", setName, duration);
        setsToExpire.put(setName, lastTickTime + duration);
        CraftingHarmonicsMod.updateSavedData();
    }

    /**
     * Checks to see if the given set is on cooldown or not
     * @param set    The set to check
     * @return       True if it is, false otherwise.
     */
    static boolean isSetOnCooldown(String set) {
        return setsOnCooldown.containsKey(set);
    }

    /**
     * Called when a set with a cooldown is removed
     * @param setName     The set name
     * @param cooldown    The cooldown
     */
    static void setWithCooldownRemoved(String setName, int cooldown) {
        // If we haven't run our first update tick: don't care.
        if(lastTickTime <= 0) return;

        // Otherwise, go ahead and say that we need to expire at some point
        LogHelper.info("Set {} on cooldown for {} ticks.", setName, cooldown);
        setsOnCooldown.put(setName, lastTickTime + cooldown);
        CraftingHarmonicsMod.updateSavedData();
    }

    /**
     * Called internally to deserialie the saved data
     * @param nbt    The NBT to read
     */
    public static void deserializeSavedGameData(NBTTagCompound nbt) {
        setsToExpire.clear();
        setsOnCooldown.clear();

        if(nbt.hasKey(SETS_TO_EXPIRE_TAG_NAME)) {
            setsToExpire.putAll(convertNbtCompoundToMap(nbt.getCompoundTag(SETS_TO_EXPIRE_TAG_NAME)));
        }

        if(nbt.hasKey(SETS_ON_COOLDOWN_TAG_NAME)) {
            setsOnCooldown.putAll(convertNbtCompoundToMap(nbt.getCompoundTag(SETS_ON_COOLDOWN_TAG_NAME)));
        }
    }

    /**
     * Serializes the setsOnCooldown list to NBT
     * @return  The output NBT
     */
    public static NBTBase serializeSetsOnCooldown() {
        return convertMapToCompoundNbt(setsOnCooldown);
    }

    /**
     * Serializes the setsToExpire list to NBT
     * @return  The output NBT
     */
    public static NBTBase serializeSetsToExpire() {
        return convertMapToCompoundNbt(setsToExpire);
    }

    /**
     * Converts an NBT tag compound into a map of strings to longs.
     * @param nbt    The NBT to parse
     * @return       The map
     */
    private static Map<String, Long> convertNbtCompoundToMap(NBTTagCompound nbt) {
        Map<String, Long> output = new HashMap<>();
        Set<String> keySet = nbt.getKeySet();
        for(String key : keySet) {
            output.put(key, nbt.getLong(key));
        }

        return output;
    }

    /**
     * Converts the map to a NBT tag compound
     * @param map    The map to convert
     * @return       The output NBT
     */
    private static NBTTagCompound convertMapToCompoundNbt(Map<String, Long> map) {
        NBTTagCompound output = new NBTTagCompound();
        for(Map.Entry<String, Long> entry : map.entrySet()) {
            output.setTag(entry.getKey(), new NBTTagLong(entry.getValue()));
        }
        return output;
    }
}
