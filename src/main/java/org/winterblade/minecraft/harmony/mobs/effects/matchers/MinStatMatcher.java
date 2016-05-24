package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.common.dto.NameValuePair;
import org.winterblade.minecraft.harmony.common.matchers.BaseStatMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"minStat"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinStatMatcher extends BaseStatMatcher implements IMobPotionEffectMatcher {
    public MinStatMatcher(NameValuePair<Integer> stat) {
        super(stat.getName(), stat.getValue(), Integer.MAX_VALUE);
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, PotionEffect effect) {
        return matches(entityLivingBase);
    }
}