package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHeightMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"minHeight"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MinHeightMatcher extends BaseHeightMatcher implements IMobShedMatcher {
    public MinHeightMatcher(int level) {super(level, Integer.MAX_VALUE);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLiving entity, ItemStack drop) {
        return matches(entity.getPosition());
    }
}