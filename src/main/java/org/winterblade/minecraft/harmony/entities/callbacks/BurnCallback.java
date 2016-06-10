package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "burn")
public class BurnCallback extends VectorBaseCallback {
    private IEntityCallback[] onSuccess;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onComplete;

    @Override
    protected void applyTo(Entity target, CallbackMetadata data) {
        BlockPos pos = new BlockPos(getPosition(target));

        // Try to just burn the spot we're looking at:
        if(burnPos(target.getEntityWorld(), pos, target, data)) return;

        // Otherwise, do some slight searching up and down...
        for(byte y = -2; y <= 2; y++) {
            if(burnPos(target.getEntityWorld(), pos.add(0,y,0), target, data)) return;
        }

        runCallbacks(onFailure, target, data);
        runCallbacks(onComplete, target, data);
    }

    private boolean burnPos(World world, BlockPos pos, Entity target, CallbackMetadata metadata) {
        if (!world.isAirBlock(pos)) return false;

        // Borrowed from the flint and steel:
        world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
        runCallbacks(onSuccess, target, metadata);
        runCallbacks(onComplete, target, metadata);
        return true;
    }
}
