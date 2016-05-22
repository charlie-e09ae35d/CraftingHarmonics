package org.winterblade.minecraft.harmony.scripting.wrappers.entity.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.ScriptInterop;

/**
 * Created by Matt on 5/22/2016.
 */
@ScriptInterop(wraps = EntityLightningBolt.class)
public class InteropEntityLightningBolt {

    /**
     * Target the lightning bolt at the given entity
     * @param entity    The entity to target
     * @return          The bolt
     */
    public static net.minecraft.entity.effect.EntityLightningBolt onEntity(EntityLivingBase entity) {
        return onEntity(entity, false);
    }

    /**
     * Target the lightning bolt at the given entity
     * @param entity        The entity to target
     * @param effectOnly    If the bolt is for effect only, or if it does damage
     * @return              The bolt
     */
    public static net.minecraft.entity.effect.EntityLightningBolt onEntity(EntityLivingBase entity, boolean effectOnly) {
        BlockPos pos = entity.getPosition();
        return new net.minecraft.entity.effect.EntityLightningBolt(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), effectOnly);
    }
}
