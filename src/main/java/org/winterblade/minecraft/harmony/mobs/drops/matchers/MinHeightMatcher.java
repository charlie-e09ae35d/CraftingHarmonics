package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeightMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"minHeight"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MinHeightMatcher extends BaseHeightMatcher implements IMobDropMatcher {
    public MinHeightMatcher(int level) {super(level, Integer.MAX_VALUE);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getEntity().getPosition());
    }
}
