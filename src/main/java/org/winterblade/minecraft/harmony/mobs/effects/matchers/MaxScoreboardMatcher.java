package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseScoreboardMatcher;
import org.winterblade.minecraft.harmony.common.dto.NameValuePair;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"maxScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxScoreboardMatcher extends BaseScoreboardMatcher implements IMobPotionEffectMatcher {
    public MaxScoreboardMatcher(NameValuePair<Integer> score) {super(score.getName(), Integer.MIN_VALUE, score.getValue());}

    @Override
    public BaseMatchResult isMatch(EntityLivingBase evt, PotionEffect drop) {
        return matches(evt.getEntityWorld(), evt);
    }
}