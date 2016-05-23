package org.winterblade.minecraft.harmony.scripting.wrappers.entity;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.ScriptInterop;

import java.util.Random;

/**
 * Created by Matt on 5/22/2016.
 */
@ScriptInterop(wraps = EntityLivingBase.class)
public class InteropEntityLivingBase extends InteropEntity {
    private final EntityLivingBase entity;

    public InteropEntityLivingBase(EntityLivingBase entity) {
        super(entity);
        this.entity = entity;
    }

    public Random getRNG() {
        return entity.getRNG();
    }

    public InteropEntityLivingBase getAITarget() {
        return new InteropEntityLivingBase(entity.getAITarget());
    }

    public InteropEntityLivingBase getLastAttacker() {
        return new InteropEntityLivingBase(entity.getLastAttacker());
    }

    public int getAge() {
        return entity.getAge();
    }

    // TODO: More stuff
}
