package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
@Operation(name = "remove")
public class RemoveOperation extends BasicOperation {
    /**
     * Serialized properties
     */
    private String what;
    private String[] from;
    private ItemStack[] with;
    private int width;
    private int height;

    /**
     * Computed properties
     */
    private RemoveMatchType matchType;
    private String modId;
    private String itemName;
    private int metadata;
    private long fromFlag;
    private List<IRemovedRecipe> removedRecipes;

    public boolean MatchesName(ItemStack recipeOutput) {
        // If we have a null output... ignore it.
        if(recipeOutput == null) return false;

        // If we really, really want to remove everything...
        if(what.equals("*") || what.equals("*:*") || what.equals("*:*:*")) return true;

        String[] name = ItemUtility.getFullyQualifiedItemName(recipeOutput.getItem()).split(":");

        switch(matchType) {
            case ItemOnly:
                return name[1].equals(itemName);
            case ModOnly:
                return name[0].equals(modId);
            case ItemAndMod:
                return name[0].equals(modId) && name[1].equals(itemName);
            case Exact:
                return name[0].equals(modId) && name[1].equals(itemName)
                        && recipeOutput.getMetadata() == metadata;
            default:
                return false;
        }
    }


    @Override
    public void init() throws OperationException {
        String[] parts = what.split(":");

        if(parts.length >= 3 && !parts[2].equals("*")) {
            // We have to deal with metadata; exact match required.
            modId = parts[0];
            itemName = parts[1];
            metadata = Integer.parseInt(parts[2]);
            matchType = RemoveMatchType.Exact;
        }
        else if(parts.length <= 1) {
            // Match the entire thing against the name
            matchType = RemoveMatchType.ItemOnly;
            itemName = what;
        } else if(parts[0].equals("*")) {
            // Match by item name:
            matchType = RemoveMatchType.ItemOnly;
            itemName = parts[1];
        } else if(parts[1].equals("*")) {
            // Match by mod name
            modId = parts[0];
            matchType = RemoveMatchType.ModOnly;
        } else {
            // Match by item and mod name
            modId = parts[0];
            itemName = parts[1];
            matchType = RemoveMatchType.ItemAndMod;
        }

        // Default from all:
        if(from == null || from.length <= 0) {
            fromFlag = FromFlags.CRAFTING.getFlag() | FromFlags.FURNACE.getFlag();
        } else {
            fromFlag = 0;
            for(String s: from) {
                try {
                    FromFlags flag = Enum.valueOf(FromFlags.class, s.toUpperCase());
                    fromFlag |= flag.getFlag();
                } catch(Exception ex) {
                    LogHelper.warn("Removed recipe type '" + s + "' is not valid.");
                }
            }
        }

        removedRecipes = new ArrayList<>();
    }

    @Override
    public void apply() {
        if(FromFlags.hasFlag(fromFlag, FromFlags.CRAFTING)) RemoveCraftingRecpies();
        if(FromFlags.hasFlag(fromFlag, FromFlags.FURNACE)) RemoveFurnaceRecpies();
    }

    @Override
    public void undo() {
        for(IRemovedRecipe removedRecipe : removedRecipes) {
            removedRecipe.Undo();
        }
    }

    /**
     * Removes recipes from the furnace, if it matches.
     */
    private void RemoveFurnaceRecpies() {
        Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
        for(Iterator<Map.Entry<ItemStack, ItemStack>> furnaceIterator = smeltingList.entrySet().iterator(); furnaceIterator.hasNext(); ) {
            Map.Entry<ItemStack, ItemStack> recipe = furnaceIterator.next();
            if(!MatchesName(recipe.getValue())) continue;
            if(with != null && with.length > 0 && !ItemStack.areItemStacksEqual(with[0], recipe.getKey())) continue;

            // We matched something:
            LogHelper.info("Removing " + recipe.getValue().getUnlocalizedName() + " from the furnace.");
            removedRecipes.add(new RemovedFurnaceRecipe(recipe));
            furnaceIterator.remove();
        }
    }

    /**
     * Removes recpies from the crafting grid, if it matches.
     */
    private void RemoveCraftingRecpies() {
        List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();

        InventoryCrafting inv = ItemUtility.simulateInventoryOf(with, width, height);

        LogHelper.info("Searching for recipes to remove for " + modId + ":" + itemName + ":" + metadata + "...");

        for(Iterator<IRecipe> recipeIterator = recipeList.iterator(); recipeIterator.hasNext(); ) {
            IRecipe recipe = recipeIterator.next();
            if(!MatchesName(recipe.getRecipeOutput())) continue;

            // Simulate the entire crafting operation on the recipe:
            if(with != null && with.length > 0 && !recipe.matches(inv, null)) continue;

            // We matched something:
            LogHelper.info("Removing " + recipe.getRecipeOutput().getUnlocalizedName());
            removedRecipes.add(new RemovedCraftingRecipe(recipe));
            recipeIterator.remove();
        }
    }

    @Override
    public int compareTo(IOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Pull to the top:
        if(!(o instanceof RemoveOperation)) return -1;

        RemoveOperation other = (RemoveOperation) o;

        // Next, sort by from:
        long compareFroms = fromFlag - other.fromFlag;
        if(compareFroms != 0) {
            return (int)compareFroms;
        }

        // Make sure we're initialized first...
        if(matchType == null) return 1;
        if(other.matchType == null) return -1;

        // Otherwise, sort by...
        int matchTypeComparison = matchType.compareTo(other.matchType);
        return (matchTypeComparison != 0)
                ? matchTypeComparison
                : what.compareTo(other.what);
    }

    private enum RemoveMatchType {
        ItemOnly,ModOnly,ItemAndMod,Exact
    }

    private enum FromFlags {
        CRAFTING, FURNACE;

        // This will be fine to use as this is computed every time at runtime
        // If we wanted to serialize it, using the ordinal would be a lot more fragile
        public long getFlag() {
            return 1 << this.ordinal();
        }

        public static boolean hasFlag(long check, FromFlags flag) {
            return (check & flag.getFlag()) != 0;
        }
    }

    private interface IRemovedRecipe {
        void Undo();
    }

    private class RemovedFurnaceRecipe implements IRemovedRecipe {
        private final ItemStack input;
        private final ItemStack output;
        private final float xp;

        public RemovedFurnaceRecipe(Map.Entry<ItemStack, ItemStack> recipe) {
            input = recipe.getKey();
            output = recipe.getValue();
            xp = FurnaceRecipes.instance().getSmeltingExperience(output);
        }

        public void Undo() {
            FurnaceRecipes.instance().addSmeltingRecipe(input, output, xp);
        }
    }

    private class RemovedCraftingRecipe implements IRemovedRecipe {
        private final IRecipe recipe;

        public RemovedCraftingRecipe(IRecipe recipe) {
            this.recipe = recipe;
        }

        public void Undo() {
            CraftingManager.getInstance().addRecipe(recipe);
        }
    }
}
