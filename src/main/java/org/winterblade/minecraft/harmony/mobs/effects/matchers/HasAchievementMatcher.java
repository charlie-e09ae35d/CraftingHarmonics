package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseHasAchievementMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"playerHasAchievement"})
@PrioritizedObject(priority = Priority.HIGH)
public class HasAchievementMatcher extends BaseHasAchievementMatcher implements IEntityMatcher {
    public HasAchievementMatcher(String achievementId) {
        super(achievementId);
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, IEntityMatcherData metadata) {
        return matches(entityLivingBase);
    }
}
