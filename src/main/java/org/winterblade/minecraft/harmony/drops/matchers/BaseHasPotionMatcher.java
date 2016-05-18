package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseHasPotionMatcher {
    private final Potion potion;

    public BaseHasPotionMatcher(Potion potion) {
        this.potion = potion;
    }

    protected BaseDropMatchResult matches(Entity entity) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseDropMatchResult.False;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;

        return new BaseDropMatchResult(entityBase.isPotionActive(potion));
    }
}