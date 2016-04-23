package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

/**
 * Operation for adding and removing a material from the smeltery.
 */
@RecipeOperation(name = "addSmelteryMelt", dependsOn = "tconstruct")
public class AddSmelteryMaterial extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack with;
    private String what;
    private int amount;

    /*
     * Computed properties
     */
    private transient MeltingRecipe recipe;


    @Override
    public void Init() throws ItemMissingException {
        if(!FluidRegistry.isFluidRegistered(what)) {
            CraftingHarmonicsMod.logger.warn(what + " is not a valid fluid.");
            throw new ItemMissingException(what + " is not a valid fluid.");
        }

        recipe = new MeltingRecipe(new RecipeMatch.Item(with, with.stackSize, amount), FluidRegistry.getFluid(what));
    }

    @Override
    public void Apply() {
        CraftingHarmonicsMod.logger.info("Adding Tinker's smeltery melt to turn '"
                + Item.REGISTRY.getNameForObject(with.getItem()) + "' into '" + what + "'.");
        TinkerRegistry.registerMelting(recipe);
    }

    @Override
    public void Undo() {
        TinkerRegistry.getAllMeltingRecipies().remove(recipe);
    }
}