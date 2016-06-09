package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "teleportRelative")
public class TeleportDimensionRelativeCallback extends TeleportBaseCallback {
    /*
     * Serialized properties
     */
    private double x;
    private double y;
    private double z;
    private double scaling = 1.0;

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim, CallbackMetadata metadata) {
        // Get their current position...
        Vec3d pos = target.getPositionVector();

        // If we're crossing dimensions, apply the scaling!
        if(target.getEntityWorld().provider.getDimension() != targetDim) {
            pos = pos.scale(scaling);
        }

        // Do the teleport...
        teleport(target, targetDim, pos, metadata);
    }
}
