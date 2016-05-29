package org.winterblade.minecraft.harmony.integration.abyssalcraft.operations;

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.abyssalcraft.AbyssalCraftUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/28/2016.
 */
@Operation(name = "AbyssalCraft.removeCrystallization", dependsOn = "abyssalcraft")
public class RemoveCrystallizerRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;
    private ItemStack with;

    /*
     * Computed properties
     */
    private Map<ItemStack, ItemStack[]> recipes = new HashMap<>();
    private Map<ItemStack, Float> xpList = new HashMap<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null && with == null) throw new OperationException("To remove an AbyssalCraft crystallization, you must specify at least one output or input ('what' or 'with').");

        recipes.clear();
        xpList.clear();

        for (Map.Entry<ItemStack, ItemStack[]> entry : AbyssalCraftUtilities.crystallizationList.entrySet()) {
            // If we have an input, just use that...
            if(with != null) {
                if(!ItemUtility.areItemsEquivalent(entry.getKey(),with)) continue;

                recipes.put(entry.getKey(), entry.getValue());
                xpList.put(entry.getKey(), CrystallizerRecipes.instance().getExperience(entry.getKey()));
                continue;
            }

            // Check if the first output matches:
            if (ItemUtility.areItemsEquivalent(entry.getValue()[0],what)) {
                recipes.put(entry.getKey(), entry.getValue());
                xpList.put(entry.getKey(), CrystallizerRecipes.instance().getExperience(entry.getKey()));
                continue;
            }

            // If we don't have a second output...
            if (entry.getValue()[1] == null) continue;

            // Check if the second output matches
            if (ItemUtility.areItemsEquivalent(entry.getValue()[1],what)) {
                recipes.put(entry.getKey(), entry.getValue());
                xpList.put(entry.getKey(), CrystallizerRecipes.instance().getExperience(entry.getKey()));
            }
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        if(with != null) {
            LogHelper.info("Removing AbyssalCraft crystallization using '" + ItemUtility.outputItemName(with) + "'.");
        } else {
            LogHelper.info("Removing AbyssalCraft crystallizations producing '" + ItemUtility.outputItemName(what) + "'.");
        }

        for(Map.Entry<ItemStack, ItemStack[]> entry : recipes.entrySet()) {
            AbyssalCraftUtilities.crystallizationList.remove(entry.getKey());
            AbyssalCraftUtilities.crystallizerXpList.remove(entry.getKey());
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(Map.Entry<ItemStack, ItemStack[]> entry : recipes.entrySet()) {
            AbyssalCraftUtilities.crystallizationList.put(entry.getKey(), entry.getValue());
            AbyssalCraftUtilities.crystallizerXpList.put(entry.getKey(), xpList.get(entry.getKey()));
        }
    }
}

