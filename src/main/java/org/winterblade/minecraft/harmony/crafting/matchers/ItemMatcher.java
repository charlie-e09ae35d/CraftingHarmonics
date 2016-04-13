package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;

/**
 * Created by Matt on 4/9/2016.
 */
@PrioritizedObject(priority = Priority.HIGHEST)
public class ItemMatcher implements IRecipeInputMatcher {
    private final Item item;

    public ItemMatcher(Item item) {
        this.item = item;
    }

    /**
     * Determine if the input ItemStack matches our target.  Do not modify any of the parameters; it will
     * only end in pain.  Or awesome.  But likely pain.
     *
     * @param input      The input from the crafting grid.
     * @param inventory  The inventory performing the craft.
     * @param output     The output item of the recipe
     * @return True if the given input matches the target.
     */
    @Override
    public boolean matches(ItemStack input, InventoryCrafting inventory,
                           ItemStack output) {
        return item.getUnlocalizedName().equals(input.getItem().getUnlocalizedName());
    }
}
