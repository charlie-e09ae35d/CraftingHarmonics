package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"maxMobHealth"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MaxMobHealthMatcher implements IMobShedMatcher {
    private final float health;

    public MaxMobHealthMatcher(float health) {
        this.health = health;
    };

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLiving The event to match
     * @param drop         The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLiving entityLiving, ItemStack drop) {
        return new BaseMatchResult(entityLiving.getHealth() <= health);
    }
}
