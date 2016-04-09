package org.winterblade.minecraft.harmony.crafting.operations;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
@RecipeOperation(name = "remove")
public class RemoveOperation extends BaseRecipeOperation {
    /**
     * Computed properties
     */
    private RemoveMatchType matchType;
    private String modId;
    private String itemName;
    private int metadata;
    private String what;

    public boolean Matches(ItemStack recipeOutput) {
        // If we have a null output... ignore it.
        if(recipeOutput == null) return false;

        // If we really, really want to remove everything...
        if(what.equals("*") || what.equals("*:*") || what.equals("*:*:*")) return true;

        String[] name = ItemRegistry.GetFullyQualifiedItemName(recipeOutput.getItem()).split(":");

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
    public void Init() throws ItemMissingException {
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
    }

    @Override
    public void Apply() {
        RemoveCraftingRecpies();
        RemoveFurnaceRecpies();
    }

    /**
     * Removes recipes from the furnace, if it matches.
     */
    private void RemoveFurnaceRecpies() {
        Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
        for(Iterator<Map.Entry<ItemStack, ItemStack>> furnaceIterator = smeltingList.entrySet().iterator(); furnaceIterator.hasNext(); ) {
            Map.Entry<ItemStack, ItemStack> recipe = furnaceIterator.next();
            if(!Matches(recipe.getValue())) continue;

            // We matched something:
            System.out.println("Removing " + recipe.getValue().getUnlocalizedName() + " from the furnace.");
            furnaceIterator.remove();
        }
    }

    /**
     * Removes recpies from the crafting grid, if it matches.
     */
    private void RemoveCraftingRecpies() {
        List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();

        for(Iterator<IRecipe> recipeIterator = recipeList.iterator(); recipeIterator.hasNext(); ) {
            IRecipe recipe = recipeIterator.next();
            if(!Matches(recipe.getRecipeOutput())) continue;

            // We matched something:
            System.out.println("Removing " + recipe.getRecipeOutput().getUnlocalizedName());
            recipeIterator.remove();
        }
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Pull to the top:
        if(!(o instanceof RemoveOperation)) return -1;

        RemoveOperation other = (RemoveOperation) o;

        // Make sure we're initialized first...
        if(matchType == null) return 1;
        if(other.matchType == null) return -1;

        // Otherwise, sort by...
        int matchTypeComparison = matchType.compareTo(other.matchType);
        return (matchTypeComparison != 0)
                ? matchTypeComparison
                : what.compareTo(other.what);
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     *
     * @param data The operation data
     */
    @Override
    protected void ReadData(ScriptObjectMirror data) {
        what = (String)data.get("what");
    }

    private enum RemoveMatchType {
        ItemOnly,ModOnly,ItemAndMod,Exact
    }
}
