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
@Operation(name = "AbyssalCraft.addTransmutation", dependsOn = "abyssalcraft")
public class AddTransmutationRecipeOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private RecipeInput with;
    private ItemStack output;
    private float xp;

    /*
     * Computed properties
     */
    private Map<ItemStack, ItemStack> recipes = new HashMap<>();

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(with == null || output == null) {
            throw new OperationException("AbyssalCraft transmutation recipes must have one input ('with') and one output ('output').");
        }

        List<ItemStack> inputs = new ArrayList<>();

        // Figure out if we're an ore dictionary or not...
        if(with.getFacsimileItem() instanceof String) {
            inputs.addAll(OreDictionary.getOres(with.getFacsimileItem().toString()));
        } else if(with.getFacsimileItem() instanceof ItemStack){
            inputs.add((ItemStack) with.getFacsimileItem());
        }

        if(inputs.size() <= 0) {
            throw new OperationException("AbyssalCraft transmutation recipes must have a valid input.");
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
        LogHelper.info("Adding AbyssalCraft transmutation recipe producing '" + ItemUtility.outputItemName(output) + "'.");
        for(Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            AbyssalCraftUtilities.transmutationList.put(entry.getKey(), entry.getValue());
            AbyssalCraftUtilities.transmutationXpList.put(entry.getKey(), xp);
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        for(ItemStack key : recipes.keySet()) {
            AbyssalCraftUtilities.transmutationList.remove(key);
            AbyssalCraftUtilities.transmutationXpList.remove(key);
        }
    }
}
