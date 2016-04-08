package org.winterblade.minecraft.harmony;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import org.winterblade.minecraft.harmony.commands.CommandHandler;
import org.winterblade.minecraft.harmony.config.ConfigManager;
import org.winterblade.minecraft.harmony.config.ConfigOperationDeserializer;
import org.winterblade.minecraft.harmony.crafting.FuelRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedNbtMatchingRecipe;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedOreNbtMatchingRecipe;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapelessNbtMatchingRecipe;
import org.winterblade.minecraft.harmony.utility.AnnotatedInstanceUtil;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPELESS;

/**
 * Created by Matt on 4/5/2016.
 */
@Mod(modid = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.MODID, version = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.VERSION)
public class CraftingHarmonicsMod {
    public static final String MODID = "craftingharmonics";
    public static final String VERSION = "1.0.1";

    private String configPath;
    private ConfigManager configManager;

    public CraftingHarmonicsMod() {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load all recipe operations (thanks mezz, who thanks cpw... so also thanks cpw)
        ConfigOperationDeserializer.CreateDeserializers(AnnotatedInstanceUtil.getRecipeOperations(event.getAsmData()));

        // Handle config
        configManager = new ConfigManager(event.getModConfigurationDirectory() + "/CraftingHarmonics/");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        GameRegistry.registerFuelHandler(FuelRegistry.getInstance());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ItemRegistry.Init();

        // Always apply the default set:
        CraftingSet defaultSet = configManager.GetSet("default");

        if(defaultSet != null) {
            defaultSet.Init();
            defaultSet.Apply();
        }

        // Link in our recipes
        RecipeSorter.register("craftingharmonics:shaped_nbt",       ShapedNbtMatchingRecipe.class,
                SHAPED,    "before:minecraft:shaped");
        RecipeSorter.register("craftingharmonics:shaped_nbt_ore",   ShapedOreNbtMatchingRecipe.class,
                SHAPED,    "after:minecraft:shaped before:forge:shapedore");
        RecipeSorter.register("craftingharmonics:shapeless_nbt",    ShapelessNbtMatchingRecipe.class,
                SHAPELESS, "after:forge:shapedore before:minecraft:shapeless");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
    }
}
