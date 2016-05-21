package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"not"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class NotMatcher implements IMobShedMatcher {
    private final MobShed composite;

    public NotMatcher(MobShed composite) {
        this.composite = composite;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity           The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        // We aren't going to run any of the callbacks from here, as they're inverted.
        BaseMatchResult result = composite.matches(entity, drop);

        // Just invert the match:
        return new BaseMatchResult(!result.isMatch());
    }
}
