package org.winterblade.minecraft.harmony.utility;

import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matt on 4/29/2016.
 */
public class SavedGameData extends WorldSavedData {
    private static final String DATA_NAME = CraftingHarmonicsMod.MODID + "_SavedGameData";

    private Set<String> loadedSets = new HashSet<>();

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

        SavedGameData instance = (SavedGameData) storage.loadData(SavedGameData.class, DATA_NAME);

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
        if(!loadedSets.add(set)) markDirty();
    }

    /**
     * Remove a set to the list of loaded sets
     * @param set    The set to remove
     */
    public void removeSet(String set) {
        // If we removed, then mark it dirty
        if(!loadedSets.remove(set)) markDirty();
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
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     *
     * @param nbt   The NBT to write to
     */
    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound loadedSetNbt = new NBTTagCompound();

        // Not the most elegant solution, but...
        for(String name : loadedSets) {
            loadedSetNbt.setBoolean(name, true);
        }

        // Write the entire compound to NBT now:
        nbt.setTag("LoadedSets", loadedSetNbt);
    }
}
