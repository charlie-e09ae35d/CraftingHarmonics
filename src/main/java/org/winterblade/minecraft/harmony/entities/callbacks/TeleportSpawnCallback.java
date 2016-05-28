package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "teleportSpawn")
public class TeleportSpawnCallback extends TeleportBaseCallback {
    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        // Current dimension:
        if(target.getEntityWorld().provider.getDimension() != targetDim) {
            BlockPos spawn = target.getEntityWorld().getSpawnPoint();
            teleport(target, targetDim, spawn.getX(), spawn.getY(), spawn.getZ());
            return;
        }

        // Another dimension entirely...
        WorldServer server = DimensionManager.getWorld(targetDim);

        if(server == null) {
            LogHelper.warn("Destination dimension {} didn't exist; not teleporting.", dimension);
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        BlockPos spawn = server.getSpawnPoint();
        teleport(target, targetDim, spawn.getX(), spawn.getY(), spawn.getZ());
    }
}
