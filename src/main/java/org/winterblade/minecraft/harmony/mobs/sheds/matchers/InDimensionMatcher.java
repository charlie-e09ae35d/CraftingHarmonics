package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseDimensionMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"inDimensions"})
@PrioritizedObject(priority = Priority.HIGHER)
public class InDimensionMatcher extends BaseDimensionMatcher implements IMobShedMatcher {
    public InDimensionMatcher(Integer[] dimensions) {super(dimensions);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity           The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLiving entity, ItemStack drop) {
        return matches(entity);
    }
}

