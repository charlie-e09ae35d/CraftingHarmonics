package org.winterblade.minecraft.harmony;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.winterblade.minecraft.harmony.config.ConfigManager;
import org.winterblade.minecraft.harmony.config.operations.CraftingSet;

import java.io.File;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
@Mod(modid = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.MODID, version = org.winterblade.minecraft.harmony.CraftingHarmonicsMod.VERSION)
public class CraftingHarmonicsMod {
    public static final String MODID = "craftingharmonics";
    public static final String VERSION = "0.1";

    private String configPath;
    private ConfigManager configManager;

    public CraftingHarmonicsMod() {
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Handle config
        configManager = new ConfigManager(event.getModConfigurationDirectory() + "/CraftingHarmonics/");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Always apply the default set:
        CraftingSet defaultSet = configManager.GetSet("default");

        if(defaultSet != null) defaultSet.Apply(CraftingManager.getInstance());
    }

    private void GetRecipeList() {
        CraftingManager craftingManager = CraftingManager.getInstance();

        System.out.println("------------------------------------------------------------------------------");
        if(craftingManager == null) {
            System.out.println("Unable to get crafting manager.");
            return;
        }

        List<IRecipe> recipeList = craftingManager.getRecipeList();

        for (int i = 0; i < recipeList.size(); i++) {
            IRecipe recipe = recipeList.get(i);
            ItemStack output = recipe.getRecipeOutput();
            if(output == null) continue;

            if(recipe instanceof ShapedRecipes) {
                ShapedRecipes shaped = (ShapedRecipes) recipe;
                ItemStack[] recipeItems = shaped.recipeItems;

                String inputs = i + " Shaped [";

                for (ItemStack recipeItem : recipeItems) {
                    if(recipeItem != null) {
                        inputs += recipeItem.getUnlocalizedName() + ", ";
                    } else {
                        inputs += "_, ";
                    }
                }

                System.out.println(inputs + "]: " + output.getUnlocalizedName());
            }
            else {
                System.out.println(i + " is Unknown Crafting Type: " + output.getUnlocalizedName());
            }
        }
        System.out.println("------------------------------------------------------------------------------");
    }
}
