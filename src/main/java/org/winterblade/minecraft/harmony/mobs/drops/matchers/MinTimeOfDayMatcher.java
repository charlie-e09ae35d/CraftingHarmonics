package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseTimeOfDayMatcher;

/**
 * Created by Matt on 5/14/2016.
 */
@Component(properties = "minTimeOfDay")
@PrioritizedObject(priority = Priority.HIGH)
public class MinTimeOfDayMatcher extends BaseTimeOfDayMatcher implements IMobDropMatcher {
    public MinTimeOfDayMatcher(long minTime) {super(minTime, Long.MAX_VALUE);}

    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getEntity().getEntityWorld());
    }
}
