package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IMobPotionEffectMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseAndMatcher;
import org.winterblade.minecraft.harmony.mobs.effects.MobPotionEffect;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"and"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AndMatcher extends BaseAndMatcher<EntityLivingBase, PotionEffect, IMobPotionEffectMatcher, MobPotionEffect> implements IMobPotionEffectMatcher {
    public AndMatcher(MobPotionEffect[] composites) {
        super(composites);
    }
}