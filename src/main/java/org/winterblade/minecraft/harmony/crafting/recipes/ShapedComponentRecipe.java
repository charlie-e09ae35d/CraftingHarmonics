package org.winterblade.minecraft.harmony.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

import java.util.*;


/**
 * Created by Matt on 4/9/2016.
 */
public class ShapedComponentRecipe extends ShapedOreRecipe {
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;

    private final int width;
    private final int height;
    private final RecipeInput[] input;
    private final RecipeComponent output;
    private static final int CHAR_A = 65;

    public ShapedComponentRecipe(int width, int height, RecipeInput[] input, RecipeComponent output) {
        super(output.getItemStack(), itemStacksToOreRecipe(RecipeInput.getFacimileItems(input), width, height));
        this.width = width;
        this.height = height;
        this.input = input;
        this.output = output;
    }

    private static Object[] itemStacksToOreRecipe(Object[] facimileItems, int width, int height) {
        String[] lines = new String[height];
        Map<Character, Object> charmap = new HashMap<>();

        /**
         * Build out the recipe's pattern
         */
        int offset = 0;
        for(int y = 0; y < height; y++) {
            lines[y] = "";
            for(int x = 0; x < width; x++) {
                if(facimileItems[offset] == null) {
                    lines[y] += " ";
                } else {
                    // This will produce increasing values of A, B, C, etc
                    char id = (char)(CHAR_A +offset);
                    lines[y] += id;
                    charmap.put(id, facimileItems[offset]);
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
        return args.toArray();
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, worldIn))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, World world)
    {
        boolean hasAtLeastOneMatcher = false;
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                int pos = subX + subY * width;
                RecipeInput target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    target = input[pos];
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                // If we're null...
                if(RecipeInput.isNullOrEmpty(target)) {
                    // .. and we need to not be, bail:
                    if(slot != null) return false;
                    // Otherwise, don't run matchers
                    continue;
                }

                // If the slot is null, and it's not supposed to be...
                if(slot == null) return false;

                // Run matchers here...
                hasAtLeastOneMatcher = true;
                if(!target.matches(slot,inv, x,y,world, pos, output.getItemStack())) return false;
            }
        }

        // Prevent bad recipes from being 'default':
        return hasAtLeastOneMatcher;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return output.getItemStack().copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.getItemStack();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] ret = new ItemStack[inv.getSizeInventory()];
        RecipeInput target;

        // Lower right
        int left = 2;
        int top = 2;

        // Find the left/top of our recipe
        for(int y = 0; y <= inv.getHeight() - height; y++) {
            // We have to iterate every column, because there might be a null item in the upper left...
            for(int x = 0; x <= inv.getWidth(); x++) {
                ItemStack slot = inv.getStackInRowAndColumn(x, y);
                if(slot == null) continue;

                if(x < left) left = x;
                if(y < top) {
                    top = y;
                    // however, once we find that top item, we can break.
                    break;
                }
            }
        }

        // Shift
        int invOffset = left + (top * inv.getWidth());

        // Grab our stack list...
        ItemStack[] stackList = ObfuscationReflectionHelper.getPrivateValue(InventoryCrafting.class, inv, 0);

        for (int i = 0; i < ret.length && i < input.length; i++)
        {
            target = input[i];

            // Make sure we have an item to work with...
            ItemStack slot = inv.getStackInSlot(i+invOffset);
            if(slot == null) continue;

            // Transform it and roll out.
            ItemStack transformed = target.applyTransformers(ItemRegistry.duplicate(slot), ForgeHooks.getCraftingPlayer());

            // We're bypassing setInventorySlotContents so as to not fire off the crafting update event
            // This doesn't prevent counts from still being wrong, but it at least does prevent Minecraft
            // matching the entire recipe list, again...
            stackList[i+invOffset] = transformed;

            ItemStack containerItem = ForgeHooks.getContainerItem(transformed);

            // If we're going to run into the GUI bug...
            if(slot.stackSize > 1 && ItemStack.areItemsEqual(slot, transformed) && slot.stackSize != transformed.stackSize) {
                // Haaaaack.  Terrible, terrible haaaack.
                // So, how this works: we can't return out of this function with a modified stack size of the same item
                // otherwise the game client gets updated to an incorrect count, so we need to get how many more/less
                // we're trying to give/take from the player and use the ret value to invoke SlotCrafting's code which
                // will later modify the counts by the appropriate amount.
                int modifiedBy = transformed.stackSize - slot.stackSize;
                slot.stackSize = modifiedBy;

                // Make sure we copy our damage, just in case:
                slot.setItemDamage(transformed.getItemDamage());
                transformed.stackSize -= modifiedBy;
                ret[i+invOffset] = slot;
            } else {
                ret[i+invOffset] = containerItem;
            }
        }

        return ret;
    }
}
