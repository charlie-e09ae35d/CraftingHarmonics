package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 5/2/2016.
 */
@RecipeOperation(name = "setHarvestLevel")
public class SetHavestLevelOperation extends BaseHarvestLevelOperation {
    /*
     * Serialized properties
     */
    private String what;
    private int to;
    private String with;

    @Override
    public void Init() throws ItemMissingException {
        Block block = Block.REGISTRY.getObject(new ResourceLocation(what));

        if(block == null) throw new ItemMissingException("Couldn't translate '" + what + "' to a valid block.");

        for (IBlockState state : block.getBlockState().getValidStates()) {
            // Add the block:
            affectedBlocks.add(new BlockHarvestabilityModifier(block, state, to, with));
        }
    }
}
