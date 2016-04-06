package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "addBrew")
public class AddBrew extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private String[] with;
    private String input;
    private String ingredient;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack inputItem;
    private transient ItemStack ingredientItem;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        if(with != null && with.length >= 2) {
            input = with[0];
            ingredient = with[1];
        }

        if(input.equals("") || ingredient.equals("")) throw new ItemMissingException("Brewing recipe is missing input or ingredient.");

        inputItem = ItemRegistry.TranslateToItemStack(input);
        if(inputItem == null) throw new RuntimeException("Unable to find requested input item '" + input + "'.");
        if(inputItem.getMaxStackSize() > 1) throw new RuntimeException("Inputs for brewing cannot be stackable.");

        ingredientItem = ItemRegistry.TranslateToItemStack(ingredient);
        if(ingredientItem == null) throw new RuntimeException("Unable to find requested ingredient item '" + ingredient + "'.");

        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding brewing recipe for " + outputItemStack.getUnlocalizedName());
        BrewingRecipeRegistry.addRecipe(inputItem, ingredientItem, outputItemStack);
    }
}
