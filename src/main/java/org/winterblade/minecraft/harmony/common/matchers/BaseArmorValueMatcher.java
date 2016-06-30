package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 6/29/2016.
 */
public abstract class BaseArmorValueMatcher {
    private final int min;
    private final int max;

    public BaseArmorValueMatcher(int min, int max) {
        this.min = min;
        this.max = max;
    }

    protected BaseMatchResult matches(Entity entity) {
        if(entity == null || !EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;

        // Get our entity and convert it over:
        EntityLivingBase entityBase = (EntityLivingBase) entity;
        int points = entityBase.getTotalArmorValue();

        return (min <= points && points <= max) ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
