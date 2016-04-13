package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;

import java.util.List;

/**
 * Calling this lower due to the loop iteration.  Man that's painful.
 *
 * Created by Matt on 4/9/2016.
 */
@PrioritizedObject(priority = Priority.LOWER)
public class OreDictionaryMatcher implements IRecipeInputMatcher {
    private final String oreDictName;

    public OreDictionaryMatcher(String oreDictName) {
        this.oreDictName = oreDictName;
    }

    /**
     * Determine if the input ItemStack matches our target.  Do not modify any of the parameters; it will
     * only end in pain.  Or awesome.  But likely pain.
     *
     * @param input     The input from the crafting grid.
     * @param inventory The inventory performing the craft.
     * @param output    The output item of the recipe
     * @return True if the given input matches the target.
     */
    @Override
    public boolean matches(ItemStack input, InventoryCrafting inventory, ItemStack output) {
        List<ItemStack> ores = OreDictionary.getOres(oreDictName);

        for(ItemStack target : ores) {
            if (target == null || input == null || target.getItem() != input.getItem()) continue;
            return true;
        }

        return false;
    }
}
