package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "addTime")
public class AddTimeCallback extends BaseEntityAndDimensionCallback {
    /*
     * Serialized properties
     */
    private int time;
    private IEntityCallback[] onSuccess;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onComplete;

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim, CallbackMetadata metadata) {
        WorldServer worldServer = DimensionManager.getWorld(targetDim);
        if(worldServer == null) {
            LogHelper.error("Attempted to set the time for a world (" + targetDim + ") that doesn't exist.");
            runCallbacks(onFailure, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        // Actually set the time now...
        worldServer.setWorldTime(worldServer.getWorldTime() + time);
        runCallbacks(onSuccess, target, metadata);
        runCallbacks(onComplete, target, metadata);
    }
}
