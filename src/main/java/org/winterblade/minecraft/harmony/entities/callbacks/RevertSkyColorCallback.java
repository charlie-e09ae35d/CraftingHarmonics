package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.world.sky.SkyModificationRegistry;

/**
 * Created by Matt on 6/2/2016.
 */
@EntityCallback(name = "revertSkyColor")
public class RevertSkyColorCallback extends BaseEntityAndDimensionCallback {
    /*
     * Serialized props
     */
    private boolean global;
    private IEntityCallbackContainer[] onSuccess;
    private IEntityCallbackContainer[] onFailure;
    private IEntityCallbackContainer[] onComplete;

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        // If we're doing it for everyone...
        if(global) {
            SkyModificationRegistry.removeModifications(targetDim);
            runCallbacks(onSuccess, target);
            runCallbacks(onComplete, target);
            return;
        }

        // Otherwise, try to run it on the target...
        runCallbacks(SkyModificationRegistry.removeModifications(targetDim, target)
                        ? onSuccess
                        : onFailure,
                target);
        runCallbacks(onComplete, target);
    }
}
