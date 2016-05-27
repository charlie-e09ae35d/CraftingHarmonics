package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "explode")
public class ExplodeCallback extends VectorBaseCallback {
    /*
     * Serialized properties
     */
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
        Vec3d pos = getPosition(target);
        target.getEntityWorld().createExplosion(target, pos.xCoord + x, pos.yCoord + y, pos.zCoord + z, strength, isSmoking);
    }

}
