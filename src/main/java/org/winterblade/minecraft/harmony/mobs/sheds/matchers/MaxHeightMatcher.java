package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeightMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"maxHeight"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MaxHeightMatcher extends BaseHeightMatcher implements IMobShedMatcher {
    public MaxHeightMatcher(int level) {super(0,level);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        return matches(entity.getPosition());
    }
}