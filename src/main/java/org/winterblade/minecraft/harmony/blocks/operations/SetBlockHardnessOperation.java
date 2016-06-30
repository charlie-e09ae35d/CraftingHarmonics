package org.winterblade.minecraft.harmony.blocks.operations;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;

/**
 * Created by Matt on 6/27/2016.
 */
@Operation(name = "setBlockHardness")
public class SetBlockHardnessOperation extends BasicOperation {
    private final Block air = Block.REGISTRY.getObject(new ResourceLocation("minecraft:air"));
    /*
     * Serialized properties
     */
    private BlockMatcher what;
    private float hardness;

    /*
     * Computed properties
     */
    private transient float prevLevel;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(hardness < 0.0) throw new OperationException("setBlockHardness must have a hardness at least 0.0");
        if(what.getBlock() == null || what.getBlock() == air) throw new OperationException("setBlockHardness could not find a valid block to adjust.");

        prevLevel = ObfuscationReflectionHelper.getPrivateValue(Block.class, what.getBlock(), "field_149782_v");
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        what.getBlock().setHardness(hardness);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        what.getBlock().setHardness(prevLevel);
    }
}
