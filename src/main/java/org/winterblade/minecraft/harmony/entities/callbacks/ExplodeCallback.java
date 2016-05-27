package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "explode")
public class ExplodeCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    // Offset coords:
    private double x;
    private double y;
    private double z;

    // Vector coordinates
    private double magnitude = Double.MIN_VALUE;
    private float yaw;
    private float pitch;

    // Actual properties of the explosion
    private float strength;
    private boolean isSmoking;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(strength < 0) throw new RuntimeException("Explosion strength cannot be less than zero.");
    }

    @Override
    protected void applyTo(Entity target) {
        // Relative coordinates:
        if (magnitude == Double.MIN_VALUE) {
            target.getEntityWorld().createExplosion(target, target.posX + x, target.posY + y, target.posZ + z, strength, isSmoking);
            return;
        }

        //
        Vec3d pos = target.getLookVec().rotatePitch((float) (pitch*Math.PI)).rotateYaw((float) (yaw*Math.PI)).scale(magnitude).add(target.getPositionVector());
        target.getEntityWorld().createExplosion(target, pos.xCoord + x, pos.yCoord + y, pos.zCoord + z, strength, isSmoking);
    }
}
