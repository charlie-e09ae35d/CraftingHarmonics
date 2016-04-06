package org.winterblade.minecraft.harmony.config.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import org.apache.commons.lang3.ArrayUtils;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/5/2016.
 */
public class AddShapedOperation implements IRecipeOperation {
    /**
     * Serialized properties:
     */
    public String output;
    public int quantity;
    public String[] shape; // 0.2 support
    public String[] with;
    public int width;
    public int height;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack[] input;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        if(with.length > 0) shape = with;

        String[] filler = new String[1];
        filler[0] = "";

        if(shape.length <= 0) throw new ItemMissingException("Shaped recipe has no inputs.");

        // There are some edge cases in here where the user might have meant one
        // thing and receives another.  Not accounting for that just now.
        // Also: these default to assuming width over height.
        switch(shape.length) {
            case 1: // This is now virtually a shapeless recipe...
                width = height = 1;
                break;
            case 2:
            case 3:
                if(width <= 0 && height <= 0) {
                    width = shape.length;
                    height = 1;
                } else if(width <= 0) {
                    width = 1;
                    height = shape.length;
                } else if(height <= 0) {
                    height = 1;
                    width = shape.length;
                }
                break;
            case 4:
                width = height = 2;
                break;
            case 5: // Uh, what?  Make it 6
                shape = ArrayUtils.addAll(shape, filler);
            case 6:
                if(height <= 0) {
                    height = 2;
                    width = 3;
                } else if(width <= 0) {
                    width = 2;
                    height = 3;
                }
                break;
            case 7:
                shape = ArrayUtils.addAll(shape, filler);
            case 8:
                shape = ArrayUtils.addAll(shape, filler);
            default:
                width = height = 3;
                break;
        }

        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        input = new ItemStack[shape.length];

        for(int i = 0; i < shape.length; i++) {
            input[i] = ItemRegistry.TranslateToItemStack(shape[i]);
        }

    }

    @Override
    public void Apply() {
        System.out.println("Adding shaped recipe for " + outputItemStack.getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(new ShapedRecipes(width, height, input, outputItemStack));
    }
}
