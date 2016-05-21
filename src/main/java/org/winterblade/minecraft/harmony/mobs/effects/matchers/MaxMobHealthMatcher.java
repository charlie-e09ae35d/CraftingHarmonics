package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"maxMobHealth"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MaxMobHealthMatcher implements IMobPotionEffectMatcher {
    private final float health;

    public MaxMobHealthMatcher(float health) {
        this.health = health;
    };

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLiving The event to match
     * @param drop         The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLiving, PotionEffect drop) {
        return new BaseMatchResult(entityLiving.getHealth() <= health);
    }
}
