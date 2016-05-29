package org.winterblade.minecraft.harmony.integration.abyssalcraft.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.abyssalcraft.AbyssalCraftUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matt on 5/28/2016.
 */
@Operation(name = "AbyssalCraft.addCrystallizer", dependsOn = "abyssalcraft")
public class AddCrystallizerRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private RecipeInput with;
    private ItemStack[] output;
    private float xp;

    /*
     * Computed properties
     */
    private Map<ItemStack, ItemStack[]> recipes = new HashMap<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(with == null) throw new OperationException("AbyssalCraft crystallization recipe must have an input ('with').");
        if(output == null || output.length <= 0) throw new OperationException("AbyssalCraft crystallization recipe must have at least one output ('output': []).");
        if(2 < output.length) throw new OperationException("AbyssalCraft crystallization recipes can have at most 2 outputs.");

        List<ItemStack> inputs = new ArrayList<>();

        // Figure out if we're an ore dictionary or not...
        if(with.getFacsimileItem() instanceof String) {
            inputs.addAll(OreDictionary.getOres(with.getFacsimileItem().toString()));
        } else if(with.getFacsimileItem() instanceof ItemStack){
            inputs.add((ItemStack) with.getFacsimileItem());
        }

        if(inputs.size() <= 0) {
            throw new OperationException("AbyssalCraft crystallization recipe must have a valid input.");
        }

        recipes.clear();

        for(ItemStack input : inputs) {
            recipes.put(input, output);
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding AbyssalCraft crystallization producing '" + ItemUtility.outputItemName(output[0]) + "'.");
        for(Map.Entry<ItemStack, ItemStack[]> entry : recipes.entrySet()) {
            AbyssalCraftUtilities.crystallizationList.put(entry.getKey(), entry.getValue());
            AbyssalCraftUtilities.crystallizerXpList.put(entry.getKey(), xp);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(Map.Entry<ItemStack, ItemStack[]> entry : recipes.entrySet()) {
            AbyssalCraftUtilities.crystallizationList.remove(entry.getKey());
            AbyssalCraftUtilities.crystallizerXpList.remove(entry.getKey());
        }
    }
}
