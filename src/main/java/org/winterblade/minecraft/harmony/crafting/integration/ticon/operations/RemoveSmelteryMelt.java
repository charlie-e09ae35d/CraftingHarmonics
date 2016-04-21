package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.ReflectedTinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
@RecipeOperation(name = "removeSmelteryMelt", dependsOn = "tconstruct")
public class RemoveSmelteryMelt extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack with;
    private FluidStack what;

    /*
     * Computed properties
     */
    private transient List<MeltingRecipe> recipes = new ArrayList<>();

    @Override
    public void Init() throws ItemMissingException {

    }

    @Override
    public void Apply() {
        recipes.clear();

        CraftingHarmonicsMod.logger.info("Removing '" + what.getFluid().getName() + "' melts from the smeltery.");
        List<MeltingRecipe> meltingRecipies = TinkerRegistry.getAllMeltingRecipies();

        for(Iterator<MeltingRecipe> recipeIterator = meltingRecipies.iterator(); recipeIterator.hasNext(); ) {
            MeltingRecipe recipe = recipeIterator.next();

            if(!matches(recipe)) continue;

            recipeIterator.remove();
            recipes.add(recipe);
        }
    }

    @Override
    public void Undo() {
        for(MeltingRecipe recipe : recipes) {
            TinkerRegistry.registerMelting(recipe);
        }
    }

    /**
     * Check if the given recipe matches this operation
     * @param recipe    The recipe to check
     * @return          True if the recipe matches
     */
    private boolean matches(MeltingRecipe recipe) {
        if(!recipe.getResult().isFluidEqual(what)) return false;

        // If that's all we're checking
        if(with == null) return true;

        return recipe.matches(with);
    }
}
