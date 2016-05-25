package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseOrMatcher;
import org.winterblade.minecraft.harmony.mobs.effects.MobPotionEffect;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher extends BaseOrMatcher<EntityLivingBase, IEntityMatcherData, IEntityMatcher, MobPotionEffect> implements IEntityMatcher {
    public OrMatcher(MobPotionEffect[] composites) {
        super(composites);
    }
}