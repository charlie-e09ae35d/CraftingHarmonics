package org.winterblade.minecraft.harmony;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.Logger;
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
import org.winterblade.minecraft.harmony.scripting.ScriptObjectReader;
import org.winterblade.minecraft.harmony.utility.AnnotationUtil;
import org.winterblade.minecraft.harmony.utility.EventHandler;

import java.util.HashMap;
import java.util.Map;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

/**
 * Created by Matt on 4/5/2016.
 */
@Mod(modid = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.MODID, version = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.VERSION)
public class CraftingHarmonicsMod {
    public static final String MODID = "craftingharmonics";
    public static final String VERSION = "@VERSION@";

    private String configPath;
    private ConfigManager configManager;

    private final static Map<String, CraftingSet> craftingSets = new HashMap<>();

    public static Logger logger;

    public CraftingHarmonicsMod() {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load all recipe operations (thanks mezz, who thanks cpw... so also thanks cpw)
        RecipeOperationRegistry.CreateDeserializers(AnnotationUtil.getRecipeOperations(event.getAsmData()));
        ScriptObjectReader.RegisterDeserializerClasses(AnnotationUtil.getScriptObjectDeserializers(event.getAsmData()));
        ComponentRegistry.registerComponents(AnnotationUtil.getComponentClasses(event.getAsmData()));

        // Setup Nashorn.
        logger = event.getModLog();

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

        // Always apply the default set:
        CraftingSet defaultSet = craftingSets.get("default");

        if(defaultSet != null) {
            defaultSet.Init();
        }

        // Link in our recipes
        RecipeSorter.register("craftingharmonics:shaped_component",       ShapedComponentRecipe.class,
                SHAPED,    "before:minecraft:shaped");
        RecipeSorter.register("craftingharmonics:shapeless_component",    ShapelessComponentRecipe.class,
                SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");

    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent evt) {
        CraftingSet defaultSet = craftingSets.get("default");

        // Apply the default set once the game has finished loading
        if(defaultSet != null) {
            defaultSet.Apply();
        }
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

}
