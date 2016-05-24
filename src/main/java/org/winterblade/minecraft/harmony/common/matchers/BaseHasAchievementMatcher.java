package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/23/2016.
 */
public abstract class BaseHasAchievementMatcher {
    private Achievement achievement;

    protected BaseHasAchievementMatcher(String achievementId) {
        achievement = null;

        // Find our achievement...
        for(Achievement it : AchievementList.ACHIEVEMENTS) {
            if(!it.statId.equals(achievementId)) continue;
            achievement = it;
            break;
        }

        if(achievement == null) {
            LogHelper.warn("Unable to find achievement '" + achievementId + "'.");
        }
    }

    protected BaseMatchResult matches(Entity entity) {
        if(entity == null || !EntityPlayerMP.class.isAssignableFrom(entity.getClass()) || achievement == null)
            return BaseMatchResult.False;

        return ((EntityPlayerMP) entity).getStatFile().hasAchievementUnlocked(achievement)
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }
}
