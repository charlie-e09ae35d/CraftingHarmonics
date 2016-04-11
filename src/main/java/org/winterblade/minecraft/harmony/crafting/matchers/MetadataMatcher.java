package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.RecipeInputMatcher;

/**
 * Created by Matt on 4/9/2016.
 */
@RecipeInputMatcher(properties = {"metadata"}, priority = Priority.HIGHEST)
public class MetadataMatcher implements IRecipeInputMatcher {
    private final int metadata;

    public MetadataMatcher(int metadata) {
        this.metadata = metadata;
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
    public boolean matches(ItemStack input, InventoryCrafting inventory, int posX, int posY,
                           World world, int targetPos, ItemStack output) {
        return metadata == OreDictionary.WILDCARD_VALUE || metadata == input.getMetadata();
    }
}
