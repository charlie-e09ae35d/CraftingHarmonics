package org.winterblade.minecraft.harmony.api.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.Priority;

import java.util.*;

/**
 * Created by Matt on 4/9/2016.
 */
public class RecipeInput {
    private static final int CHAR_A = 65;
    private final PriorityQueue<RecipeInputMatcherData> matchers = new PriorityQueue<>();
    private final List<IItemStackTransformer> transformerList = new ArrayList<>();
    private Object facsimileItem;

    /**
     * Add a matcher to this RecipeInput
     * @param matcher
     * @param priority
     */
    public void addMatcher(IRecipeInputMatcher matcher, Priority priority) {
        matchers.add(new RecipeInputMatcherData(matcher, priority));
    }

    /**
     * Adds a transformer to the transformer list.
     * @param transformer   The transformer to add.
     */
    public void addTransformer(IItemStackTransformer transformer) {
        transformerList.add(transformer);
    }

    /**
     * Determine if the input ItemStack matches our target.
     *
     * @param input     The input from the crafting grid.
     * @param inventory The inventory performing the craft.
     * @param output    The output item of the recipe
     * @return          True if the given input matches the target.
     */
    public boolean matches(ItemStack input, InventoryCrafting inventory,
                           ItemStack output) {

        // Iterate our matchers, finding the first one that fails.
        for(RecipeInputMatcherData matcher : matchers) {
            if(!matcher.getMatcher().matches(input, inventory, output)) return false;
        }

        // Wow, we got here?
        return true;
    }

    /**
     * Apply any transformers and return the item stack.
     * @param input  The item stack to transform.
     * @param craftingPlayer
     * @return       The transformed item stack
     */
    public ItemStack applyTransformers(ItemStack input, EntityPlayer craftingPlayer) {
        // Make sure that we get proper counts:
        input.stackSize = 0;

        for(IItemStackTransformer transformer : transformerList) {
            input = transformer.transform(input, craftingPlayer);
            if(input == null) return null;
        }

        return input;
    }

    /**
     * Checks if the given RecipeInput is null or empty
     * @param input The RecipeInput to check
     * @return      True if it's null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(RecipeInput input) {
        return input == null || input.matchers.size() == 0;
    }

    public static Object[] getFacsimileItems(RecipeInput[] recipeInputs) {
        Object[] output = new Object[recipeInputs.length];

        for (int i = 0; i < recipeInputs.length; i++) {
            output[i]  = recipeInputs[i].getFacsimileItem();
        }

        return output;
    }

    public Object getFacsimileItem() {
        return facsimileItem;
    }

    public void setFacsimileItem(ItemStack item) {
        this.facsimileItem = item;
    }

    public void setFacimileItem(String oreDictName) {
        this.facsimileItem = oreDictName;
    }

    /**
     * Outputs an input to character map of the given width/height
     * @param width     The recipe width
     * @param height    The recipe height
     * @param input     The recipe inputs to use
     * @return          The character map
     */
    public static RecipeInputMap toInputMap(int width, int height, RecipeInput[] input) {
        String[] lines = new String[height];
        Map<Character, Object> charmap = new HashMap<>();

        /**
         * Build out the recipe's pattern
         */
        int offset = 0;
        for(int y = 0; y < height; y++) {
            lines[y] = "";
            for(int x = 0; x < width; x++) {
                if(input.length <= offset || input[offset] == null || input[offset].getFacsimileItem() == null) {
                    lines[y] += " ";
                } else {
                    // This will produce increasing values of A, B, C, etc
                    char id = (char)(CHAR_A +offset);
                    lines[y] += id;
                    charmap.put(id, input[offset].getFacsimileItem());
                }

                offset++;
            }
        }

        return new RecipeInputMap(lines, charmap);
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

        @Override
        public String toString() {
            return "[" + priority + ": " + matcher + "]";
        }
    }

    @Override
    public String toString() {
        return "RecipeInput{" +
                "matchers=" + matchers +
                ", transformerList=" + transformerList +
                ", facsimileItem=" + facsimileItem +
                '}';
    }

    /**
     * An input map
     */
    public static class RecipeInputMap {
        private final String[] lines;
        private final Map<Character, Object> data;
        private final Object[] args;

        public RecipeInputMap(String[] lines, Map<Character, Object> data) {
            this.lines = lines;
            this.data = data;

            List<Object> args = new ArrayList<>();
            args.add(false); // This will turn off mirroring.
            Collections.addAll(args, lines);
            for(Map.Entry<Character, Object> kv : data.entrySet()) {
                args.add(kv.getKey());
                args.add(kv.getValue());
            }
            this.args = args.toArray();
        }

        public Object[] toArgs() {
            return args;
        }
    }
}
