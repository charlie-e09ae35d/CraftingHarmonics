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
@Operation(name = "setLightLevel")
public class SetLightLevelOperation extends BasicOperation {
    private final Block air = Block.REGISTRY.getObject(new ResourceLocation("minecraft:air"));
    /*
     * Serialized properties
     */
    private BlockMatcher what;
    private float lightLevel;

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
        if(lightLevel < 0.0 || 1.0 < lightLevel) throw new OperationException("setLightLevel can only have a light level between 0.0 and 1.0.");
        if(what.getBlock() == null || what.getBlock() == air) throw new OperationException("setLightLevel could not find a valid block to adjust.");

        prevLevel = ((int)ObfuscationReflectionHelper.getPrivateValue(Block.class, what.getBlock(), "field_149784_t"))/15.0F;
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        what.getBlock().setLightLevel(lightLevel);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        what.getBlock().setLightLevel(prevLevel);
    }
}
