package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseScoreboardMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"maxScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxScoreboardMatcher extends BaseScoreboardMatcher implements IMobShedMatcher {
    public MaxScoreboardMatcher(ScoreboardMatchData score) {
        super(score.getName(), Integer.MIN_VALUE, score.getValue(), score.getPlayer());
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase evt, ItemStack drop) {
        return matches(evt.getEntityWorld(), evt);
    }
}