package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 5/16/2016.
 */
public abstract class BaseDamageEntityMatcher {
    private final float health;
    private final DamageSource damageType;

    public BaseDamageEntityMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute, boolean isCreative) {
        this.health = health;
        this.damageType = new DamageSource(damageType);
        if(isUnblockable) this.damageType.setDamageBypassesArmor();
        if(isAbsolute) this.damageType.setDamageIsAbsolute();
        if(isCreative) this.damageType.setDamageAllowedInCreativeMode();
    }

    protected BaseMatchResult damageEntity(Entity entity) {
        if(entity == null || !(entity instanceof EntityLivingBase)) return BaseMatchResult.True;

        return new BaseMatchResult(true, () -> {
            if(0 <= health) {
                entity.attackEntityFrom(damageType, health);
            } else {
                ((EntityLivingBase)entity).heal(health);
            }
        });
    }
}
