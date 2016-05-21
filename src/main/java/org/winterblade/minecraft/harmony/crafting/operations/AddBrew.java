package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.common.crafting.operations.BaseAddOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.List;

/**
 * Created by Matt on 4/6/2016.
 */
@Operation(name = "addBrew")
public class AddBrew extends BaseAddOperation {
    private RecipeComponent[] with;
    private RecipeComponent input;
    private RecipeComponent ingredient;
    private static List<IBrewingRecipe> recipes;
    private BrewingRecipe recipe;

    static {
        // Get our brewing registry...
        recipes = ObfuscationReflectionHelper.getPrivateValue(BrewingRecipeRegistry.class, null, 0);
    }

    @Override
    public void init() throws OperationException {
        super.init();

        if(with != null && with.length >= 2) {
            input = with[0];
            ingredient = with[1];
        }

        if(input.equals("") || ingredient.equals("")) throw new OperationException("Brewing recipe is missing input or ingredient.");

        if(input == null) throw new RuntimeException("Unable to find requested input item " + input.toString());
        if(input.hasNbt()) {
            LogHelper.warn("NBT support for brews isn't done yet because it's considered an edge case - NBT + " +
                    "stack size 1? - if you need this, please let me know!");
        }
        if(input.getItemStack().getMaxStackSize() > 1) throw new RuntimeException("Inputs for brewing cannot be stackable.");

        if(ingredient == null) throw new RuntimeException("Unable to find requested ingredient item " + ingredient.toString());
        recipe = new BrewingRecipe(input.getItemStack(), ingredient.getItemStack(), output.getItemStack());
    }

    @Override
    public void apply() {
        LogHelper.info("Adding brewing recipe for  " + output.toString());
        BrewingRecipeRegistry.addRecipe(recipe);
    }

    @Override
    public void undo() {
        recipes.remove(recipe);
    }
}
