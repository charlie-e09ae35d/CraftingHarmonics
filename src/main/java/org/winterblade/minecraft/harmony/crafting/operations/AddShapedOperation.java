package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.ArrayUtils;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.*;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "addShaped")
public class AddShapedOperation extends BaseAddOperation {
    /**
     * Serialized properties:
     */
    private String[] shape; // 0.2 support
    private String[] with;
    private int width;
    private int height;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack[] input;
    private transient Object[] inputOreDict;
    private transient ItemStack outputItemStack;
    private transient boolean isOreDict;

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
        if (outputItemStack == null)
            throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        input = new ItemStack[shape.length];
        inputOreDict = new Object[shape.length];

        for(int i = 0; i < shape.length; i++) {
            if(shape[i] == "") {
                inputOreDict[i] = input[i] = null;
            }
            else if(ItemRegistry.IsOreDictionaryEntry(shape[i])) {
                isOreDict = true;
                inputOreDict[i] = ItemRegistry.GetOreDictionaryName(shape[i]);
            } else {
                inputOreDict[i] = input[i] = ItemRegistry.TranslateToItemStack(shape[i]);
            }
        }

    }

    @Override
    public void Apply() {
        System.out.println("Adding shaped recipe for " + outputItemStack.getUnlocalizedName());
        CraftingManager.getInstance().addRecipe(isOreDict
                ? CreateOreDictRecipe()
                : new ShapedRecipes(width, height, input, outputItemStack));
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
                    char id = (char)(65+offset);
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
        return new ShapedOreRecipe(outputItemStack, args.toArray());
    }
}
