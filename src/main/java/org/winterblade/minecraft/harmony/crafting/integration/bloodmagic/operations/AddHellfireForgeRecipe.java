package org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.recipe.TartaricForgeRecipe;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.integration.bloodmagic.ReflectedBloodMagicRegistry;

/**
 * Created by Matt on 4/22/2016.
 */
@RecipeOperation(name = "addHellfireForgeRecipe", dependsOn = "BloodMagic")
public class AddHellfireForgeRecipe extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private double minSouls;
    private double drain;
    private RecipeInput[] with;

    /*
     * Computed properties
     */
    private transient TartaricForgeRecipe recipe;

    @Override
    public void Init() throws ItemMissingException {
        recipe = new TartaricForgeRecipe(output, minSouls, drain, RecipeInput.getFacsimileItems(with));
    }

    @Override
    public void Apply() {
        CraftingHarmonicsMod.logger.info("Adding Hellfire Forge recipe for '" + ItemRegistry.outputItemName(output) + "'.");
        ReflectedBloodMagicRegistry.addHellfireForgeRecipe(recipe);
    }

    @Override
    public void Undo() {
        ReflectedBloodMagicRegistry.removeHellfireForgeRecipe(recipe);
    }
}
