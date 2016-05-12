package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseHasPotionMatcher {
    private final Potion potion;

    public BaseHasPotionMatcher(Potion potion) {
        this.potion = potion;
    }

    protected boolean matches(Entity entity) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return false;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        return entityBase.isPotionActive(potion);
    }
}
