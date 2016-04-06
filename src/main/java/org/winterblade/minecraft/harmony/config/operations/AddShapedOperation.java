package org.winterblade.minecraft.harmony.config.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/5/2016.
 */
public class AddShapedOperation implements IAddOperation {
    /**
     * Serialized properties:
     */
    public String output;
    public int quantity;
    public String[] shape; // 0.2 support
    public String[] with;

    /**
     * Actual items and whatnot
     */
    private transient int size;
    private transient ItemStack[] input;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        if(with.length > 0) shape = with;

        size = shape.length == 4 ? 2 : 3;
        if(quantity <= 0 || quantity > 64) quantity = 1;

        input = new ItemStack[shape.length];

        for(int i = 0; i < shape.length; i++) {
            input[i] = ItemRegistry.TranslateToItemStack(shape[i]);
        }

        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);

        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");
    }

    @Override
    public IRecipe CreateRecipe() {
        return new ShapedRecipes(size, size, input, outputItemStack);
    }
}
