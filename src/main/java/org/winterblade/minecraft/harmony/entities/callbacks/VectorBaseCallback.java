package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Matt on 5/26/2016.
 */
public abstract class VectorBaseCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    // Offset coords:
    protected double x;
    protected double y;
    protected double z;

    // Vector coordinates
    protected double magnitude = Double.MIN_VALUE;
    protected float yaw;
    protected float pitch;

    protected Vec3d getPosition(Entity target) {
        return magnitude == Double.MIN_VALUE
                ? target.getPositionVector().addVector(x, y, z)
                : target.getLookVec().rotatePitch((float) (pitch*Math.PI)).rotateYaw((float) (yaw*Math.PI)).scale(magnitude).add(target.getPositionVector());

    }
}
