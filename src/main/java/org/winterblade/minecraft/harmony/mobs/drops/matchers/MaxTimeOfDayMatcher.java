package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseTimeOfDayMatcher;

/**
 * Created by Matt on 5/14/2016.
 */
@Component(properties = "maxTimeOfDay")
@PrioritizedObject(priority = Priority.HIGH)
public class MaxTimeOfDayMatcher extends BaseTimeOfDayMatcher implements IMobDropMatcher {
    public MaxTimeOfDayMatcher(long time) {super(0, time);}

    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getEntity().getEntityWorld());
    }
}
