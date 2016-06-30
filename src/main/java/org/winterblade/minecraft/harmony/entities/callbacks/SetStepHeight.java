package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 6/28/2016.
 */
@EntityCallback(name = "setStepHeight")
public class SetStepHeight extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private float height = 0.6f;
    private IEntityCallback[] onSuccess;

    @Override
    protected void applyTo(Entity target, CallbackMetadata metadata) {
        target.stepHeight = height;
        if(onSuccess != null) runCallbacks(onSuccess, target, metadata);
    }
}
