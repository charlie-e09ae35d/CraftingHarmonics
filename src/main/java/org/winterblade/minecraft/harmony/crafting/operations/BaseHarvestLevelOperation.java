package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/2/2016.
 */
public abstract class BaseHarvestLevelOperation extends BasicOperation {


    /*
     * Computed properties
     */
    protected transient List<BlockHarvestabilityModifier> affectedBlocks = new ArrayList<>();

    @Override
    public void apply() {
        affectedBlocks.forEach(BlockHarvestabilityModifier::apply);
    }

    @Override
    public void undo() {
        affectedBlocks.forEach(BlockHarvestabilityModifier::undo);
    }

    protected static class BlockHarvestabilityModifier {
        private final Block block;
        private final IBlockState blockState;
        private final int oldLevel;
        private final int newLevel;
        private final String oldTool;
        private final String newTool;
        private final String resName;

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
            resName = Block.REGISTRY.getNameForObject(block).toString();
        }

        /**
         * Update the block's harvest level.
         */
        void apply() {
            LogHelper.info("Updating " + resName + "'s harvest level from " + oldTool + ":" + oldLevel
                    + " to " + newTool + ":" + newLevel);
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
