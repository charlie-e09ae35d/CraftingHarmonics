package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.Vec3d;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "smite")
public class SmiteCallback extends VectorBaseCallback {
    private boolean effectOnly;
    private IEntityCallback[] onComplete;

    @Override
    protected void applyTo(Entity target, CallbackMetadata data) {
        Vec3d pos = getPosition(target);
        EntityLightningBolt bolt = new EntityLightningBolt(target.getEntityWorld(), pos.xCoord, pos.yCoord, pos.zCoord, effectOnly);
        target.getEntityWorld().addWeatherEffect(bolt);
        runCallbacks(onComplete, target);
    }
}
