package org.winterblade.minecraft.harmony.misc.operations;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 5/2/2016.
 */
@Operation(name = "setHarvestLevel")
public class SetHavestLevelOperation extends BaseHarvestLevelOperation {
    /*
     * Serialized properties
     */
    private String what;
    private int to;
    private String with;

    @Override
    public void init() throws OperationException {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(what));

        if(block == null) throw new OperationException("Couldn't translate '" + what + "' to a valid block.");

        for (IBlockState state : block.getBlockState().getValidStates()) {
            // Add the block:
            affectedBlocks.add(new BlockHarvestabilityModifier(block, state, to, with));
        }
    }
}
