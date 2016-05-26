package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.FMLServerHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class TeleportBaseCallback extends BaseEntityCallback {
    /*
         * Serialized properties
         */
    protected int dimension = Integer.MIN_VALUE;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(dimension != Integer.MIN_VALUE && !DimensionManager.isDimensionRegistered(dimension)) {
            throw new RuntimeException("Unable to create teleporter to dimension " + dimension + ", as it isn't registered.");
        }
    }

    @Override
    protected final void applyTo(Entity target) {
        int targetDim = dimension == Integer.MIN_VALUE ? target.dimension : dimension;
        applyTeleport(target, targetDim);
    }

    protected abstract void applyTeleport(Entity target, int targetDim);

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
            return;
        }

        // The slightly harder version...

        // Start with a sanity check:
        WorldServer destServer = DimensionManager.getWorld(dimension);
        if(destServer == null) {
            LogHelper.info("Cannot teleport '{}' to non-existent dimension {}.", target.getName(), dimension);
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
    }
}
