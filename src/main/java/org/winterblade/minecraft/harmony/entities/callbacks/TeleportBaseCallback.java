package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class TeleportBaseCallback extends BaseEntityAndDimensionCallback {
    private IEntityCallbackContainer[] onSuccess;
    protected IEntityCallbackContainer[] onFailure;
    protected IEntityCallbackContainer[] onComplete;

    /**
     * Teleport the given target to the specified dimension and {@link Vec3d} position.
     * @param target       The target
     * @param dimension    The target dimension
     * @param pos          The target position
     */
    protected final void teleport(Entity target, int dimension, Vec3d pos) {
        teleport(target, dimension, pos.xCoord, pos.yCoord, pos.zCoord);
    }

    /**
     * Teleport the given target to the specified dimension and coordinates
     * @param target       The target
     * @param dimension    The target dimension
     * @param x            The x-coordinate of the teleport.
     * @param y            The y-coordinate of the teleport.
     * @param z            The z-coordinate of the teleport.
     */
    protected final void teleport(Entity target, int dimension, double x, double y, double z) {
        int prevDim = target.getEntityWorld().provider.getDimension();
        // If we're a simple transfer...
        if(prevDim == dimension) {
            target.setPositionAndUpdate(x, y, z);
            runCallbacks(onSuccess, target);
            runCallbacks(onComplete, target);
            return;
        }

        // The slightly harder version...

        // Start with a sanity check:
        WorldServer destServer = DimensionManager.getWorld(dimension);
        if(destServer == null) {
            LogHelper.info("Cannot teleport '{}' to non-existent dimension {}.", target.getName(), dimension);
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        // This supports other things than players...?
        FMLServerHandler.instance().getServer().getPlayerList()
                .transferEntityToWorld(target, prevDim, DimensionManager.getWorld(prevDim), destServer);
        target.setPositionAndUpdate(x, y, z);

        // Extra check added from looking at the RFTools code; apparently the end == weird, go figure...
        if (prevDim == 1) {
            target.setPositionAndUpdate(x, y, z);
            destServer.spawnEntityInWorld(target);
            destServer.updateEntityWithOptionalForce(target, false);
        }

        runCallbacks(onSuccess, target);
        runCallbacks(onComplete, target);
    }
}
