package org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.ReflectedBloodMagicRegistry;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;

/**
 * Created by Matt on 4/21/2016.
 */
@RecipeOperation(name = "addBloodAltarRecipe", dependsOn = "BloodMagic")
public class AddAltarRecipe extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack[] with;
    private int minTier;
    private int lpCost;
    private int consumeRate;
    private int drainRate;

    /*
     * Computed properties
     */
    AltarRecipeRegistry.AltarRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        recipe = new AltarRecipeRegistry.AltarRecipe(Arrays.asList(with), what, EnumAltarTier.values()[minTier-1], lpCost, consumeRate, drainRate, false);
    }

    @Override
    public void Apply() {
        CraftingHarmonicsMod.logger.info("Adding blood altar recipe for '" + ItemRegistry.outputItemName(what) + "'.");
        ReflectedBloodMagicRegistry.addAltarRecipe(recipe);
    }

    @Override
    public void Undo() {
        ReflectedBloodMagicRegistry.removeAltarRecipe(recipe);
    }
}
