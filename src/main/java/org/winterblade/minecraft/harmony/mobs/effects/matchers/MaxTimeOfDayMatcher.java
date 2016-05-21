package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseTimeOfDayMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = "maxTimeOfDay")
@PrioritizedObject(priority = Priority.HIGH)
public class MaxTimeOfDayMatcher extends BaseTimeOfDayMatcher implements IMobPotionEffectMatcher {
    public MaxTimeOfDayMatcher(long time) {super(0, time);}

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, PotionEffect drop) {
        return matches(entity.getEntityWorld());
    }
}
