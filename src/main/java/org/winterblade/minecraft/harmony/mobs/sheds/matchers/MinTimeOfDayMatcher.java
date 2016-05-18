package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseTimeOfDayMatcher;

/**
 * Created by Matt on 5/14/2016.
 */
@Component(properties = "minTimeOfDay")
@PrioritizedObject(priority = Priority.HIGH)
public class MinTimeOfDayMatcher extends BaseTimeOfDayMatcher implements IMobShedMatcher {
    public MinTimeOfDayMatcher(long minTime) {super(minTime, Long.MAX_VALUE);}

    @Override
    public BaseDropMatchResult isMatch(EntityLiving entity, ItemStack drop) {
        return matches(entity.getEntityWorld());
    }
}