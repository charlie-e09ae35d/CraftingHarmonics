package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/2/2016.
 */
public abstract class BaseHarvestLevelOperation extends BaseRecipeOperation {


    /*
     * Computed properties
     */
    protected transient List<BlockHarvestabilityModifier> affectedBlocks = new ArrayList<>();

    @Override
    public void Apply() {
        affectedBlocks.forEach(BlockHarvestabilityModifier::apply);
    }

    @Override
    public void Undo() {
        affectedBlocks.forEach(BlockHarvestabilityModifier::undo);
    }

    protected static class BlockHarvestabilityModifier {
        private final Block block;
        private final IBlockState blockState;
        private final int oldLevel;
        private final int newLevel;
        private final String oldTool;
        private final String newTool;

        /**
         * Defines a modification to the block harvestability
         * @param block         The block to affect
         * @param blockState    The block state to affect
         * @param newLevel      The new level to apply it to
         */
        BlockHarvestabilityModifier(Block block, IBlockState blockState, int newLevel) {
            this(block, blockState, newLevel, block.getHarvestTool(blockState));
        }

        public BlockHarvestabilityModifier(Block block, IBlockState blockState, int newLevel, String newTool) {
            this.block = block;
            this.blockState = blockState;
            this.newLevel = newLevel;
            this.newTool = newTool;
            this.oldLevel = block.getHarvestLevel(blockState);
            this.oldTool = block.getHarvestTool(blockState);
        }

        /**
         * Update the block's harvest level.
         */
        void apply() {
            block.setHarvestLevel(newTool, newLevel, blockState);
        }

        /**
         * Revert the block's harvest level.
         */
        void undo() {
            block.setHarvestLevel(oldTool, oldLevel, blockState);
        }
    }
}
