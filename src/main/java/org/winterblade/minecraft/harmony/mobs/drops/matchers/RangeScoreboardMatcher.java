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
@Component(properties = {"rangeScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class RangeScoreboardMatcher extends BaseScoreboardMatcher implements IMobDropMatcher {
    public RangeScoreboardMatcher(ScoreboardMatchData score) {
        super(score);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param event The event to match
     * @param drop  The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent event, ItemStack drop) {
        if(event.getSource() == null || event.getSource().getEntity() == null) return BaseMatchResult.False;
        return matches(event.getSource().getEntity().getEntityWorld(), event.getSource().getEntity());
    }
}