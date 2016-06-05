package org.winterblade.minecraft.harmony.integration.botania.operations;

import net.minecraft.block.state.IBlockState;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;

import java.util.Optional;

/**
 * Created by Matt on 6/5/2016.
 */
@Operation(name = "Botania.addPureDaisy", dependsOn = "Botania")
public class AddPureDaisyOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private BlockMatcher with;
    private BlockMatcher output;
    private int time;

    /*
     * Computed properties
     */
    private transient RecipePureDaisy recipe;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        Optional<IBlockState> outputState = output.getFirstState();
        if(!outputState.isPresent()) throw new OperationException("Could not find a proper block output for the pure daisy.");

        recipe = time <= 0 ? new RecipePureDaisy(with.getBlock(), outputState.get()) : new RecipePureDaisy(with.getBlock(), outputState.get(), time);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding recipe for {} to the Botania pure daisy.", output.getFirstState().get().toString());
        BotaniaAPI.pureDaisyRecipes.add(recipe);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        BotaniaAPI.pureDaisyRecipes.remove(recipe);
    }
}
