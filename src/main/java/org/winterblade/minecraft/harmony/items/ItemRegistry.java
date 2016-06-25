package org.winterblade.minecraft.harmony.items;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Created by Matt on 6/22/2016.
 */
public class ItemRegistry {
    private ItemRegistry() {}

    public static final ItemRegistry instance = new ItemRegistry();

    private final Multiset<RecipeInput> preventedItems = HashMultiset.create();

    /**
     * Called to determine if an item should be allowed to be used
     * @param evt    The event to check
     * @return       True if it should be cancelled, false otherwise.
     */
    public boolean shouldCancelUse(PlayerInteractEvent evt) {
        for (RecipeInput preventedItem : preventedItems) {
            if(ItemUtility.recipeInputsMatch(preventedItem.getFacsimileItem(), evt.getItemStack())) return true;
        }
        return false;
    }


    /**
     * Adds the given stacks to the list of items to prevent using.
     * @param stacks    The stacks to prevent.
     */
    public void preventUse(@Nonnull Set<RecipeInput> stacks) {
        preventedItems.addAll(stacks);
    }

    /**
     * Removes the given stacks from the list of items to prevent using
     * @param stacks    The stacks to allow again.
     */
    public void allowUse(@Nonnull Set<RecipeInput> stacks) {
        preventedItems.removeAll(stacks);
    }
}
