package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "teleportBed")
public class TeleportBedCallback extends TeleportBaseCallback {
    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim, CallbackMetadata metadata) {
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not teleporting target ({}) to their bed, as they aren't a player.", target.getClass().getName());
            runCallbacks(onFailure, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) target;
        BlockPos bedLocation = player.getBedLocation(targetDim);
        teleport(player, targetDim, bedLocation.getX(), bedLocation.getY(), bedLocation.getZ(), metadata);
    }
}
