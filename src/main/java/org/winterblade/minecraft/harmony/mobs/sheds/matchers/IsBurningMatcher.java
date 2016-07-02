package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsBurningMatcher;

/**
 * Created by Matt on 7/1/2016.
 */
@Component(properties = {"isBurning"})
public class IsBurningMatcher extends BaseIsBurningMatcher implements IMobShedMatcher {
    public IsBurningMatcher(boolean shouldBeBurning) {
        super(shouldBeBurning);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLivingBase The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLivingBase, ItemStack drop) {
        return matches(entityLivingBase);
    }
}
