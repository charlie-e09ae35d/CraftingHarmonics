package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
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
    private IEntityCallbackContainer[] onSuccess;
    private IEntityCallbackContainer[] onFailure;
    private IEntityCallbackContainer[] onComplete;

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        WorldServer worldServer = DimensionManager.getWorld(targetDim);
        if(worldServer == null) {
            LogHelper.error("Attempted to set the time for a world (" + targetDim + ") that doesn't exist.");
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        // Actually set the time now...
        worldServer.setWorldTime(worldServer.getWorldTime() + time);
        runCallbacks(onSuccess, target);
        runCallbacks(onComplete, target);
    }
}
