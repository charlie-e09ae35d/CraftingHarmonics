package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseScoreboardMatcher;

/**
 * Created by Matt on 5/28/2016.
 */
@Component(properties = {"isScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class IsScoreboardMatcher extends BaseScoreboardMatcher implements IMobDropMatcher {
    public IsScoreboardMatcher(ScoreboardMatchData score) {
        super(score.withMinValue().withMaxValue());
    }

    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        if(evt.getSource() == null || evt.getSource().getEntity() == null) return BaseMatchResult.False;
        return matches(evt.getSource().getEntity().getEntityWorld(), evt.getSource().getEntity());
    }
}
