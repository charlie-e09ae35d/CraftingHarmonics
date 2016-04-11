package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.ArrayUtils;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedComponentRecipe;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedNbtMatchingRecipe;
import org.winterblade.minecraft.harmony.crafting.recipes.ShapedOreNbtMatchingRecipe;

import java.util.*;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShaped")
public class AddShapedOperation extends BaseAddOperation {
    public static final int CHAR_A = 65;
    /**
     * Serialized properties:
     */
    private RecipeInput[] shape; // 0.2 support
    private RecipeInput[] with;
    private int width;
    private int height;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();
        if(with.length > 0) shape = with;

        RecipeInput[] filler = new RecipeInput[1];
        filler[0] = new RecipeInput();

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
    }

    @Override
    public void Apply() {
        System.out.println("Adding shaped recipe for " + output.toString());
        CraftingManager.getInstance().addRecipe(new ShapedComponentRecipe(width, height, shape, output));
    }
}
