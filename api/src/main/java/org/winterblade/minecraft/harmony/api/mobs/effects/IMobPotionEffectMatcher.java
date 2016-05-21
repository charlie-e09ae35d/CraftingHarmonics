package org.winterblade.minecraft.harmony.api.mobs.effects;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;

/**
 * A matcher for a mob potion effect event; implementations should also be annotated with @Component and @PrioritizedObject
 */
public interface IMobPotionEffectMatcher extends IMobMatcher<EntityLivingBase,PotionEffect> {}
