package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseCooldownMatcher;

/**
 * Created by Matt on 5/22/2016.
 */
@Component(properties = "cooldown")
@PrioritizedObject(priority = Priority.LOWER)
public class CooldownMatcher extends BaseCooldownMatcher implements IEntityMatcher {
    public CooldownMatcher(int cooldown) {
        super(cooldown);
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, IEntityMatcherData metadata) {
        return matches(entityLivingBase);
    }
}
