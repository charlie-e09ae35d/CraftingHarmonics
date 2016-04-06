package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapelessRecipes;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShapeless")
public class AddShapelessOperation extends BaseRecipeOperation {
    /**
     * Serialized properties:
     */
    private String output;
    private int quantity;
    private String[] with;

    /**
     * Actual items and whatnot
     */
    private transient List<ItemStack> input;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        if(with.length <= 0) throw new ItemMissingException("Shaped recipe has no inputs.");

        input = new ArrayList<>();

        for(String item : with) {
            ItemStack inputItem = ItemRegistry.TranslateToItemStack(item);
            if(inputItem == null) continue;
            input.add(inputItem);
        }

        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding shapeless recipe for " + outputItemStack.getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(new ShapelessRecipes(outputItemStack, input));
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o instanceof AddShapelessOperation))
            return o.getClass().getSimpleName().compareTo(AddShapelessOperation.class.getSimpleName());

        // Otherwise, sort on name:
        return output.compareTo(((AddShapelessOperation) o).output);
    }
}
