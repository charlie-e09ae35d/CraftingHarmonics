package org.winterblade.minecraft.harmony.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * Created by Matt on 4/9/2016.
 */
public interface IRecipeInputMatcher {
    /**
     * Determine if the input ItemStack matches our target.  Do not modify any of the parameters; it will
     * only end in pain.  Or awesome.  But likely pain.
     *
     * @param input         The input from the crafting grid.
     * @param inventory     The inventory performing the craft.
     * @param output        The output item of the recipe
     * @return              True if the given input matches the target.
     */
    boolean matches(ItemStack input, InventoryCrafting inventory,
                    ItemStack output);
}
