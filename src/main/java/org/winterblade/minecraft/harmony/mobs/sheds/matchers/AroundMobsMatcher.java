package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNearbyMobMatcher;
import org.winterblade.minecraft.harmony.mobs.MobCountMatcher;

/**
 * Created by Matt on 5/15/2016.
 */
@Component(properties = {"aroundMobs"})
@PrioritizedObject(priority = Priority.LOWER)
public class AroundMobsMatcher extends BaseNearbyMobMatcher implements IMobShedMatcher {
    public AroundMobsMatcher(MobCountMatcher matcher) {
        super(matcher.getWhat(), matcher.getDist(), matcher.getMin(), matcher.getMax());
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(EntityLiving entity, ItemStack drop) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
