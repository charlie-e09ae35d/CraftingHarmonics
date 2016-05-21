package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseDamageEntityMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"damageOnShed", "sheddingDamageType", "sheddingDamageIsUnblockable", "sheddingDamageIsAbsolute", "sheddingDamageIsCreative"})
@PrioritizedObject(priority = Priority.LOWEST) // Only bother creating it if we're going through with the event
public class DamageOnShedMatcher extends BaseDamageEntityMatcher implements IMobShedMatcher {
    public DamageOnShedMatcher(float health) {
        this(health, "generic");
    }

    public DamageOnShedMatcher(float health, String damageType) {
        this(health, damageType, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable) {
        this(health, damageType, isUnblockable, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute) {
        this(health, damageType, isUnblockable, isAbsolute, false);
    }

    public DamageOnShedMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute, boolean isCreative) {
        super(health, damageType, isUnblockable, isAbsolute, isCreative);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLiving The event to match
     * @param drop         The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLiving, ItemStack drop) {
        return damageEntity(entityLiving);
    }
}
