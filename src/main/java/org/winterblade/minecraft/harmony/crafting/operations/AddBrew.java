package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "addBrew")
public class AddBrew extends BaseAddOperation {
    private RecipeComponent[] with;
    private RecipeComponent input;
    private RecipeComponent ingredient;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();

        if(with != null && with.length >= 2) {
            input = with[0];
            ingredient = with[1];
        }

        if(input.equals("") || ingredient.equals("")) throw new ItemMissingException("Brewing recipe is missing input or ingredient.");

        if(input == null) throw new RuntimeException("Unable to find requested input item " + input.toString());
        if(input.hasNbt()) {
            System.out.println("NBT support for brews isn't done yet because it's considered an edge case - NBT + " +
                    "stack size 1? - if you need this, please let me know!");
        }
        if(input.getItemStack().getMaxStackSize() > 1) throw new RuntimeException("Inputs for brewing cannot be stackable.");

        if(ingredient == null) throw new RuntimeException("Unable to find requested ingredient item " + ingredient.toString());
    }

    @Override
    public void Apply() {
        System.out.println("Adding brewing recipe for  " + output.toString());
        BrewingRecipeRegistry.addRecipe(input.getItemStack(), ingredient.getItemStack(), output.getItemStack());
    }
}
