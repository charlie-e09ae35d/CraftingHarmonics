package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.RecipeInputMatcher;

import java.util.List;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * Calling this lower due to the loop iteration.  Man that's painful.
 *
 * Created by Matt on 4/9/2016.
 */
@RecipeInputMatcher(property = "oredict", priority = Priority.LOWER)
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
     * @param posX      The X position of the input in the inventory
     * @param posY      The Y position of the input in the inventory
     * @param world     The world the crafting is happening in.
     * @param targetPos The position of the target in the target list
     * @param output    The output item of the recipe
     * @return True if the given input matches the target.
     */
    @Override
    public boolean matches(ItemStack input, InventoryCrafting inventory, int posX, int posY, World world, int targetPos, ItemStack output) {
        List<ItemStack> ores = OreDictionary.getOres(oreDictName);

        for(ItemStack target : ores) {
            if (target == null || input == null || target.getItem() != input.getItem()) continue;
            return true;
        }

        return false;
    }
}
