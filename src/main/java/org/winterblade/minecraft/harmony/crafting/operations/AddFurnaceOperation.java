package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addFurnace")
public class AddFurnaceOperation extends BaseRecipeOperation {
    /**
     * Serialized properties:
     */
    private String output;
    private int quantity;
    private String with;
    private float experience;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack inputItem;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        inputItem = ItemRegistry.TranslateToItemStack(with);
        if(inputItem == null) throw new RuntimeException("Unable to find requested input item '" + with + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding furnace recipe for " + outputItemStack.getUnlocalizedName());
        FurnaceRecipes.instance().addSmeltingRecipe(inputItem, outputItemStack, experience);
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o instanceof AddFurnaceOperation))
            return o.getClass().getSimpleName().compareTo(AddFurnaceOperation.class.getSimpleName());

        // Otherwise, sort on name:
        return output.compareTo(((AddFurnaceOperation) o).output);
    }
}
