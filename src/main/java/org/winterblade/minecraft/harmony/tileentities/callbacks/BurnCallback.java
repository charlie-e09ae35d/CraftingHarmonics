package org.winterblade.minecraft.harmony.tileentities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 6/4/2016.
 */
@TileEntityCallback(name = "burn")
public class BurnCallback extends CoordinateBaseCallback {
    private ITileEntityCallback[] onSuccess;
    private ITileEntityCallback[] onFailure;
    private ITileEntityCallback[] onComplete;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        // Make the default y be above the block if we're not going horizontal or vertical.
        if(x == 0 && y == 0 && z == 0) y = 1;
    }

    /**
     * Apply an action to the target.
     *
     * @param target The target to apply to.
     * @param data   Any event data to deal with.
     */
    @Override
    protected void applyTo(TileEntity target, CallbackMetadata data) {
        BlockPos pos = new BlockPos(getPosition(target));

        // Try to just burn the spot we're looking at:
        if(burnPos(target.getWorld(), pos, target)) return;

        // Otherwise, do some slight searching up and down...
        for(byte y = -2; y <= 2; y++) {
            if(burnPos(target.getWorld(), pos.add(0,y,0), target)) return;
        }

        runCallbacks(onFailure, target);
        runCallbacks(onComplete, target);
    }

    private boolean burnPos(World world, BlockPos pos, TileEntity target) {
        if (!world.isAirBlock(pos)) return false;

        // Borrowed from the flint and steel:
        world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
        runCallbacks(onSuccess, target);
        runCallbacks(onComplete, target);
        return true;
    }
}
