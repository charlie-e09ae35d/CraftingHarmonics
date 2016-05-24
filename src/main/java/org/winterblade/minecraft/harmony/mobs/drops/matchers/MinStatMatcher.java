package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.dto.NameValuePair;
import org.winterblade.minecraft.harmony.common.matchers.BaseStatMatcher;

/**
 * Created by Matt on 5/23/2016.
 */
@Component(properties = {"minStat"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinStatMatcher extends BaseStatMatcher implements IMobDropMatcher {
    public MinStatMatcher(NameValuePair<Integer> stat) {
        super(stat.getName(), stat.getValue(), Integer.MAX_VALUE);
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
        return event.getSource() != null ? matches(event.getSource().getEntity()) : BaseMatchResult.False;
    }
}