package org.winterblade.minecraft.harmony.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.Priority;

import java.util.PriorityQueue;

/**
 * Created by Matt on 4/9/2016.
 */
public class RecipeInput {
    private PriorityQueue<RecipeInputMatcherData> matchers = new PriorityQueue<>();

    /**
     * Add a matcher to this RecipeInput
     * @param matcher
     * @param priority
     */
    public void addMatcher(IRecipeInputMatcher matcher, Priority priority) {
        matchers.add(new RecipeInputMatcherData(matcher, priority));
    }

    /**
     * Determine if the input ItemStack matches our target.
     *
     * @param input     The input from the crafting grid.
     * @param inventory The inventory performing the craft.
     * @param posX      The X position of the input in the inventory
     * @param posY      The Y position of the input in the inventory
     * @param world     The world the crafting is happening in.
     * @param targetPos The position of the target in the target list
     * @param output    The output item of the recipe
     * @return          True if the given input matches the target.
     */
    public boolean matches(ItemStack input, InventoryCrafting inventory, int posX, int posY,
                           World world, int targetPos, ItemStack output) {

        // Iterate our matchers, finding the first one that fails.
        for(RecipeInputMatcherData matcher : matchers) {
            if(!matcher.getMatcher().matches(input, inventory, posX, posY, world, targetPos, output)) return false;
        }

        // Wow, we got here?
        return true;
    }

    private class RecipeInputMatcherData implements Comparable<RecipeInputMatcherData> {
        private final IRecipeInputMatcher matcher;
        private final Priority priority;

        public RecipeInputMatcherData(IRecipeInputMatcher matcher, Priority priority) {
            this.matcher = matcher;
            this.priority = priority;
        }

        @Override
        public int compareTo(RecipeInputMatcherData o) {
            // Sort by priority, then by name.
            if(priority != o.priority) return priority.ordinal() - o.priority.ordinal();
            return matcher.getClass().getName().compareTo(o.matcher.getClass().getName());
        }

        public IRecipeInputMatcher getMatcher() {
            return matcher;
        }
    }
}
