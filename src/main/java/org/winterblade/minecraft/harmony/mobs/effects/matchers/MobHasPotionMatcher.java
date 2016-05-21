package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHasPotionMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"mobHasPotionEffect"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class MobHasPotionMatcher extends BaseHasPotionMatcher implements IMobPotionEffectMatcher {
    public MobHasPotionMatcher(Potion potion) {
        super(potion);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity    The event to match
     * @param drop      The drop that will be generated if true
     * @return          True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, PotionEffect drop) {
        return matches(entity);
    }
}