package org.winterblade.minecraft.harmony.utility;

import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.SetManager;
import org.winterblade.minecraft.harmony.entities.callbacks.StopTimeCommand;

import java.util.*;

/**
 * Created by Matt on 4/29/2016.
 */
public class SavedGameData extends WorldSavedData {
    private static final String DATA_NAME = CraftingHarmonicsMod.MODID + "_SavedGameData";

    private Set<String> loadedSets = new HashSet<>();
    private Map<String, Set<String>> appliedPlayers = new HashMap<>();

    public SavedGameData() {
        this(DATA_NAME);
    }

    public SavedGameData(String s) {
        super(s);
    }

    /**
     * Gets the saved game data from the given world
     * @param world    The world to get it from
     * @return         The saved game data
     */
    public static SavedGameData get(World world) {
        MapStorage storage = world.getMapStorage();

        SavedGameData instance = (SavedGameData) storage.getOrLoadData(SavedGameData.class, DATA_NAME);

        // If we got a good instance...
        if(instance != null) return instance;

        // Otherwise, new it up...
        instance = new SavedGameData();
        storage.setData(DATA_NAME, instance);
        return instance;
    }

    /**
     * Get the list of loaded sets from our save
     * @return  A copy of the list of loaded sets
     */
    public Set<String> getLoadedSets() {
        return ImmutableSet.copyOf(loadedSets);
    }

    /**
     * Add a set to the list of loaded sets
     * @param set    The set to add
     */
    public void addSet(String set) {
        // If we added, then mark it dirty
        if(loadedSets.add(set)) markDirty();
    }

    /**
     * Remove a set to the list of loaded sets
     * @param set    The set to remove
     */
    public void removeSet(String set) {
        // If we removed, then mark it dirty
        if(loadedSets.remove(set)) markDirty();
    }

    /**
     * Get a list of all the players a particular operation has been applied to.
     * @param opId    The operation ID to pull
     * @return        The set of all applied player IDs.
     */
    public Set<String> getAppliedPlayerIdsForOp(String opId) {
        return appliedPlayers.containsKey(opId) ? ImmutableSet.copyOf(appliedPlayers.get(opId)) : ImmutableSet.of();
    }

    /**
     * Add a the given player to the given operation ID
     * @param opId    The operation ID to add to
     * @param id      The player ID to add
     */
    public void addPlayerForOperation(String opId, String id) {
        if(!appliedPlayers.containsKey(opId)) appliedPlayers.put(opId, new HashSet<>());
        if(appliedPlayers.get(opId).add(id)) markDirty();
    }

    /**
     * Remove a player from the given operation ID
     * @param opId    The operation ID to remove from
     * @param id      The player ID to remove
     */
    public void removePlayerForOperation(String opId, String id) {
        if(appliedPlayers.containsKey(opId) && appliedPlayers.get(opId).remove(id)) markDirty();
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     *
     * @param nbt   The NBT to read from
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        // Load our sets, if we have them...
        if(nbt.hasKey("LoadedSets")) {
            loadedSets.clear();
            loadedSets.addAll(nbt.getCompoundTag("LoadedSets").getKeySet());
        }

        if(nbt.hasKey("AppliedPlayers")) {
            appliedPlayers.clear();

            NBTTagCompound tag = nbt.getCompoundTag("AppliedPlayers");
            Set<String> opIds = tag.getKeySet();

            for(String opId : opIds) {
                appliedPlayers.put(opId, new HashSet<>(tag.getCompoundTag(opId).getKeySet()));
            }
        }

        StopTimeCommand.deserializeTimeStops(nbt);
        SetManager.deserializeSavedGameData(nbt);
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     *
     * @param nbt   The NBT to write to
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound loadedSetNbt = new NBTTagCompound();

        // Not the most elegant solution, but...
        for(String name : loadedSets) {
            loadedSetNbt.setBoolean(name, true);
        }

        // Write the entire compound to NBT now:
        nbt.setTag("LoadedSets", loadedSetNbt);

        // Deal with applied sets per player
        NBTTagCompound appliedPlayersTag = new NBTTagCompound();

        for(Map.Entry<String, Set<String>> entry : appliedPlayers.entrySet()) {
            NBTTagCompound opTag = new NBTTagCompound();
            for(String id : entry.getValue()) {
                opTag.setBoolean(id, true);
            }
            appliedPlayersTag.setTag(entry.getKey(), opTag);
        }
        nbt.setTag("AppliedPlayers", appliedPlayersTag);
        nbt.setTag(StopTimeCommand.RUNNING_TIME_STOPS, StopTimeCommand.serializeTimeStops());
        nbt.setTag(SetManager.SETS_TO_EXPIRE_TAG_NAME, SetManager.serializeSetsToExpire());
        nbt.setTag(SetManager.SETS_ON_COOLDOWN_TAG_NAME, SetManager.serializeSetsOnCooldown());
        return nbt;
    }
}
