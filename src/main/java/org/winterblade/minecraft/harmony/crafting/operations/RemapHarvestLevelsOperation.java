package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 5/2/2016.
 */
@RecipeOperation(name = "remapHarvestLevel")
public class RemapHarvestLevelsOperation extends BaseHarvestLevelOperation {
    /*
     * Serialized properties
     */
    private int from;
    private int to;
    private String with;


    @Override
    public void Init() throws ItemMissingException {
        affectedBlocks.clear();
        if(with.equals("")) with = null;

        for(ResourceLocation res : Block.REGISTRY.getKeys()) {
            Block block = Block.REGISTRY.getObject(res);
            if(block == null) continue;

            for (IBlockState state : block.getBlockState().getValidStates()) {
                // If we're not the harvest level, or it's the wrong tool:
                if (from != block.getHarvestLevel(state)
                        || (with != null && !with.equals(block.getHarvestTool(state)))) continue;

                // Add the block:
                affectedBlocks.add(new BlockHarvestabilityModifier(block, state, to));
            }
        }
    }
}
