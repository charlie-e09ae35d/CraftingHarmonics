package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"damageOnShed", "sheddingDamageType", "sheddingDamageIsUnblockable", "sheddingDamageIsAbsolute", "sheddingDamageIsCreative"})
@PrioritizedObject(priority = Priority.LOWEST) // Only bother creating it if we're going through with the event
public class DamageOnShedMatcher implements IMobShedMatcher {
    private final float health;
    private final DamageSource damageType;

    public DamageOnShedMatcher(float health) {
        this(health, "generic");
    }

    public DamageOnShedMatcher(float health, String damageType) {
        this(health, damageType, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable) {
        this(health, damageType, false, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute) {
        this(health, damageType, false, false, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute, boolean isCreative) {
        this.health = health;
        this.damageType = new DamageSource(damageType);
        if(isUnblockable) this.damageType.setDamageBypassesArmor();
        if(isAbsolute) this.damageType.setDamageIsAbsolute();
        if(isCreative) this.damageType.setDamageAllowedInCreativeMode();
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLiving The event to match
     * @param drop         The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(EntityLiving entityLiving, ItemStack drop) {
        return new BaseDropMatchResult(true, () -> {
            if(0 <= health) {
                entityLiving.attackEntityFrom(damageType, health);
            } else {
                entityLiving.heal(health);
            }
        });
    }
}
