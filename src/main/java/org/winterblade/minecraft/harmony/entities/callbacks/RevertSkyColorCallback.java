package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
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
    private IEntityCallback[] onSuccess;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onComplete;

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim, CallbackMetadata metadata) {
        // If we're doing it for everyone...
        if(global) {
            SkyModificationRegistry.removeModifications(targetDim);
            runCallbacks(onSuccess, target, metadata);
            runCallbacks(onComplete, target, metadata);
            return;
        }

        // Otherwise, try to run it on the target...
        runCallbacks(SkyModificationRegistry.removeModifications(targetDim, target)
                        ? onSuccess
                        : onFailure,
                target, metadata);
        runCallbacks(onComplete, target, metadata);
    }
}
