package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.ArrayUtils;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;
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
    private RecipeComponent[] shape; // 0.2 support
    private RecipeComponent[] with;
    private int width;
    private int height;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack[] input;
    private transient Object[] inputOreDict;
    private transient boolean isOreDict;
    private transient boolean isNbt;

    @Override
    public void Init() throws ItemMissingException {
        super.Init();
        if(with.length > 0) shape = with;

        RecipeComponent[] filler = new RecipeComponent[1];
        filler[0] = null;

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

        input = new ItemStack[shape.length];
        inputOreDict = new Object[shape.length];

        for(int i = 0; i < shape.length; i++) {
            if(shape[i] == null) {
                inputOreDict[i] = input[i] = null;
            }
            else if(shape[i].isOreDict()) {
                isOreDict = true;
                inputOreDict[i] = shape[i].getOreDictName();
            } else {
                inputOreDict[i] = input[i] = shape[i].getItemStack();

                // See if we need to do NBT matching...
                if(input[i] != null && !isNbt && input[i].hasTagCompound()) isNbt = true;
            }
        }

    }

    @Override
    public void Apply() {
        System.out.println("Adding shaped recipe for " + output.getItemStack().getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(isOreDict ? CreateOreDictRecipe() : CreateStandardRecipe());
    }

    /**
     * Turns our regular recipe into the proper type of recipe
     * @return The IRecipe
     */
    private IRecipe CreateStandardRecipe() {
        if(!isNbt) return new ShapedRecipes(width, height, input, output.getItemStack());
        return new ShapedNbtMatchingRecipe(width, height, input, output.getItemStack());
    }

    /**
     * Turns our ore dictionary map into the desired format for the ShapedOreRecipe
     * @return  The ShapedOreRecipe
     */
    private IRecipe CreateOreDictRecipe() {
        String[] lines = new String[height];
        Map<Character, Object> charmap = new HashMap<>();

        /**
         * Build out the recipe's pattern
         */
        int offset = 0;
        for(int y = 0; y < height; y++) {
            lines[y] = "";
            for(int x = 0; x < width; x++) {
                if(inputOreDict[offset] == null) {
                    lines[y] += " ";
                } else {
                    // This will produce increasing values of A, B, C, etc
                    char id = (char)(CHAR_A +offset);
                    lines[y] += id;
                    charmap.put(id, inputOreDict[offset]);
                }

                offset++;
            }
        }

        /**
         * Build out our arguments list...
         */
        List<Object> args = new ArrayList<>();
        args.add(false); // This will turn off mirroring.
        Collections.addAll(args, lines);
        for(Map.Entry<Character, Object> kv : charmap.entrySet()) {
            args.add(kv.getKey());
            args.add(kv.getValue());
        }

        // The args will get automatically expanded
        return isNbt
                ? new ShapedOreRecipe(output.getItemStack(), args.toArray())
                : new ShapedOreNbtMatchingRecipe(output.getItemStack(), args.toArray());
    }
}
