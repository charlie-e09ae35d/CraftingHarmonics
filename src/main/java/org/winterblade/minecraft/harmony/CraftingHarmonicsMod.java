package org.winterblade.minecraft.harmony;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.commands.CommandHandler;
import org.winterblade.minecraft.harmony.config.ConfigManager;
import org.winterblade.minecraft.harmony.crafting.ComponentRegistry;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeOperationRegistry;
import org.winterblade.minecraft.harmony.crafting.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedComponentRecipe;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapelessComponentRecipe;
import org.winterblade.minecraft.harmony.proxies.CommonProxy;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.utility.AnnotationUtil;
import org.winterblade.minecraft.harmony.utility.EventHandler;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.SavedGameData;

import java.util.*;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

/**
 * Created by Matt on 4/5/2016.
 */
@Mod(modid = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.MODID, version = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.VERSION,
    dependencies = "required-after:NashornLib@[1.9.0-1.8.77-1.2.1,)")
public class CraftingHarmonicsMod {
    public static final String MODID = "craftingharmonics";
    public static final String VERSION = "@VERSION@";

    private String configPath;
    private static ConfigManager configManager;

    @SidedProxy(clientSide = "org.winterblade.minecraft.harmony.proxies.ClientProxy",
            serverSide = "org.winterblade.minecraft.harmony.proxies.ServerProxy")
    private static CommonProxy proxy;

    private final static Map<String, CraftingSet> craftingSets = new HashMap<>();
    private final static Set<String> initializedSets = new HashSet<>();
    private final static Set<String> appliedSets = new HashSet<>();

    private static EnumDifficulty prevDifficulty = null;
    private static SavedGameData savedGameData;

    public CraftingHarmonicsMod() {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load all recipe operations (thanks mezz, who thanks cpw... so also thanks cpw)
        RecipeOperationRegistry.CreateDeserializers(AnnotationUtil.getRecipeOperations(event.getAsmData()));
        ComponentRegistry.registerComponents(AnnotationUtil.getComponentClasses(event.getAsmData()));

        // Handle config
        configManager = new ConfigManager(event.getModConfigurationDirectory() + "/CraftingHarmonics/");

        // Register event bus
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        PacketHandler.registerMessages();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelRegistry.getInstance());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemRegistry.Init();

        // Now that our item registry is set up, process our set files.
        configManager.processSetFiles();

        // Link in our recipes
        RecipeSorter.register("craftingharmonics:shaped_component",       ShapedComponentRecipe.class,
                SHAPED,    "before:minecraft:shaped");
        RecipeSorter.register("craftingharmonics:shapeless_component",    ShapelessComponentRecipe.class,
                SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");

    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        prevDifficulty = getDifficulty();
        event.registerServerCommand(new CommandHandler());
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent evt) {
        savedGameData = SavedGameData.get(DimensionManager.getWorld(0));
        proxy.onStarted(evt);
    }

    /**
     * Adds an operation to a set; creates the set if it doesn't exist.
     * @param setName   The set name to add to.
     * @param operation The operation to add.
     */
    public static void AddOperationToSet(String setName, IRecipeOperation operation) {
        if(!craftingSets.containsKey(setName)) craftingSets.put(setName, new CraftingSet());

        craftingSets.get(setName).AddOperation(operation);
    }

    /**
     * Gets a list of all valid set names
     * @return  The list of valid sets.
     */
    public static List<String> getAllSets() {
        return ImmutableList.copyOf(craftingSets.keySet());
    }

    /**
     * Initialize all the sets we have now; is idempotent
     */
    public static void initSets() {
        for(Map.Entry<String, CraftingSet> set : craftingSets.entrySet()) {
            // Init sets once:
            if(initializedSets.contains(set.getKey())) continue;

            set.getValue().Init();
            initializedSets.add(set.getKey());
        }
    }

    /**
     * Checks to see if the given set name is a valid set.
     * @param set    The set to check
     * @return       True if the set exists, false otherwise.
     */
    public static boolean isValidSet(String set) {
        return craftingSets.containsKey(set);
    }

    /**
     * Apply the given list of sets; is idempotent
     * @param sets   The sets to apply
     * @return       If at least one set was added.
     */
    public static boolean applySets(String[] sets) {
        boolean appliedNewSet = false;
        for(String set : sets) {
            appliedNewSet = applySet(set) || appliedNewSet;
        }

        return appliedNewSet;
    }

    /**
     * Applies a single set
     * @param set    The set to apply
     * @return      True if the set was applied; false otherwise
     */
    public static boolean applySet(String set) {
        if(appliedSets.contains(set) || !craftingSets.containsKey(set)) return false;

        craftingSets.get(set).Apply();
        appliedSets.add(set);
        if(savedGameData != null) savedGameData.addSet(set);
        return true;
    }

    /**
     * Undo a given set
     * @param set   The set to undo
     * @return      True if a set was undone, false otherwise
     */
    public static boolean undoSet(String set) {
        // Only undo an applied set:
        if(!appliedSets.contains(set) || !craftingSets.containsKey(set)) return false;

        craftingSets.get(set).Undo();
        appliedSets.remove(set);
        if(savedGameData != null) savedGameData.removeSet(set);
        return true;
    }

    /**
     * Clears sets, such as when you're joining a server.
     */
    public static void clearSets() {
        // Restore original crafting behavior first.
        for(String set : appliedSets) {
            if(!craftingSets.containsKey(set)) continue;
            craftingSets.get(set).Undo();
        }

        // Clear all the things!
        appliedSets.clear();
        initializedSets.clear();
        craftingSets.clear();
    }

    /**
     * Reload all configs for the server
     * @param server    The server to reload it on
     */
    public static void reloadConfigs(MinecraftServer server) {
        // Reload the configs:
        String[] sets = appliedSets.toArray(new String[appliedSets.size()]);
        clearSets();
        configManager.reload();
        initSets();
        applySets(sets);
        syncAllConfigs(server);
    }

    /**
     * Resync all configs using the current server instance
     */
    public static void syncAllConfigs() {
        syncAllConfigs(FMLCommonHandler.instance().getMinecraftServerInstance());
    }

    /**
     * Re-sync all configs
     * @param server    The server to sync them on.
     */
    public static void syncAllConfigs(MinecraftServer server) {
        // Sync out our new configs
        List<EntityPlayerMP> playerList = server.getPlayerList().getPlayerList();
        for(EntityPlayerMP player : playerList) {
            PacketHandler.synchronizeConfig(NashornConfigProcessor.getInstance().getCache(), player);
        }
    }

    public static String getDifficultyName(EnumDifficulty difficulty) {
        return difficulty.name().toLowerCase();
    }

    public static EnumDifficulty getDifficulty() {
        return DimensionManager.getWorld(0).getDifficulty();
    }

    /**
     * Checks to see if the difficulty has changed, and reload the configs if so
     */
    public static void checkDifficultyChanged() {
        EnumDifficulty curDifficulty = getDifficulty();
        if(curDifficulty == prevDifficulty) return;

        boolean removedConfigs = false;

        // Unload the previous difficulty, if we had one loaded:
        if(prevDifficulty != null) {
            removedConfigs = undoSet(getDifficultyName(prevDifficulty));
        }

        prevDifficulty = curDifficulty;
        if(applySets(new String[] { getDifficultyName(curDifficulty)}) || removedConfigs) {
            LogHelper.info("Difficulty set; reloading configs...");

            // Re-sync the applied configs.
            syncAllConfigs(FMLCommonHandler.instance().getMinecraftServerInstance());
        }
    }

    /**
     * Get the list of applied sets.
     * @return  The applied sets.
     */
    public static Set<String> getAppliedSets() {
        // Yes, returning an immutable set is hypocritical...
        return ImmutableSet.copyOf(appliedSets);
    }

    /**
     * Used to apply the base sets
     */
    public static void applyBaseSets() {
        // apply our base data
        initSets();
        applySets(new String[]{"default",
                getDifficultyName(getDifficulty())});

        // Load saved sets:
        Set<String> loadedSets = savedGameData.getLoadedSets();
        applySets(loadedSets.toArray(new String[loadedSets.size()]));
    }
}
