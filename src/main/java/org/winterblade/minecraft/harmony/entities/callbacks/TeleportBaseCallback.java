package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class TeleportBaseCallback extends BaseEntityAndDimensionCallback {
    private IEntityCallback[] onSuccess;
    protected IEntityCallback[] onFailure;
    protected IEntityCallback[] onComplete;
    private boolean unsafeTeleport;
    private int safeRadius = 5;

    /**
     * Teleport the given target to the specified dimension and {@link Vec3d} position.
     * @param target       The target
     * @param dimension    The target dimension
     * @param pos          The target position
     * @param metadata     The metadata to apply
     */
    protected final void teleport(Entity target, int dimension, Vec3d pos, CallbackMetadata metadata) {
        int prevDim = target.getEntityWorld().provider.getDimension();
        // If we're a simple transfer...
        if(prevDim == dimension) {
            pos = findNearestSafeLanding(target.getEntityWorld(), pos);

            // Make sure we found a safe landing spot (or we're in unsafe mode)...
            if(pos == null) {
                LogHelper.info("Unable to find a safe area to teleport to at the target coordinates.");
                runCallbacks(onFailure, target, metadata);
                runCallbacks(onComplete, target, metadata);
                return;
            }

            target.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);
            runCallbacks(onSuccess, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        // The slightly harder version...

        // Start with a sanity check:
        WorldServer destServer = DimensionManager.getWorld(dimension);
        if(destServer == null) {
            LogHelper.info("Cannot teleport '{}' to non-existent dimension {}.", target.getName(), dimension);
            runCallbacks(onFailure, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        pos = findNearestSafeLanding(destServer, pos);

        // Make sure we found a safe landing spot (or we're in unsafe mode)...
        if(pos == null) {
            LogHelper.info("Unable to find a safe area to teleport to at the target coordinates.");
            runCallbacks(onFailure, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        // This supports other things than players...?
        FMLServerHandler.instance().getServer().getPlayerList()
                .transferEntityToWorld(target, prevDim, DimensionManager.getWorld(prevDim), destServer);
        target.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);

        // Extra check added from looking at the RFTools code; apparently the end == weird, go figure...
        if (prevDim == 1) {
            target.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);
            destServer.spawnEntityInWorld(target);
            destServer.updateEntityWithOptionalForce(target, false);
        }

        runCallbacks(onSuccess, target, metadata);
        runCallbacks(onComplete, target, metadata);
    }

    /**
     * Teleport the given target to the specified dimension and coordinates
     * @param target       The target
     * @param dimension    The target dimension
     * @param x            The x-coordinate of the teleport.
     * @param y            The y-coordinate of the teleport.
     * @param z            The z-coordinate of the teleport.
     * @param metadata     The metadata to use
     */
    protected final void teleport(Entity target, int dimension, double x, double y, double z, CallbackMetadata metadata) {
        teleport(target, dimension, new Vec3d(x, y, z), metadata);
    }

    private Vec3d findNearestSafeLanding(World target, Vec3d pos) {
        if(unsafeTeleport || isSafe(target, pos)) return center(pos);

        Vec3d newPos;
        // This is ugly and I should probably feel bad for doing it...
        for(int m = 1; m < safeRadius; m++) {
            for(int x = -1; x <= 1; x++) {
                for(int z = -1; z <= 1; z++) {
                    for(int y = -1; y <= 1; y++) {
                        if(x == 0 && y == 0 && z == 0) continue;
                        newPos = pos.addVector(x*m,y*m,z*m);
                        if (isSafe(target, newPos)) return center(newPos);
                    }
                }
            }
        }

        return null;
    }

    private Vec3d center(Vec3d pos) {
        return new Vec3d(Math.round(pos.xCoord) + 0.5, pos.yCoord, Math.round(pos.zCoord) + 0.5);
    }

    private boolean isSafe(World target, Vec3d vecPos) {
        BlockPos pos = new BlockPos(vecPos);
        return target.isAirBlock(pos) && target.isAirBlock(pos.up()) && target.isSideSolid(pos.down(), EnumFacing.UP);
    }
}
