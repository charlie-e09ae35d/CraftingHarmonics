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
 * Created by Matt on 5/28/2016.
 */
@Component(properties = {"isScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class IsScoreboardMatcher extends BaseScoreboardMatcher implements IMobShedMatcher {
    public IsScoreboardMatcher(ScoreboardMatchData score) {
        super(score.withMinValue().withMaxValue());
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        return matches(entity.getEntityWorld(), entity);
    }
}