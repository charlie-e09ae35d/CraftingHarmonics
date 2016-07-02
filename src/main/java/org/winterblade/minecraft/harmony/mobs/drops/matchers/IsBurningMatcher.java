package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsBurningMatcher;

/**
 * Created by Matt on 7/1/2016.
 */
@Component(properties = {"isBurning"})
public class IsBurningMatcher extends BaseIsBurningMatcher implements IMobDropMatcher {
    public IsBurningMatcher(boolean shouldBeBurning) {
        super(shouldBeBurning);
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
        return matches(event.getEntity());
    }
}
