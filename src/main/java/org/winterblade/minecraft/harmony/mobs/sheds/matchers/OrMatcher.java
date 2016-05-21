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
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher implements IMobShedMatcher {
    private final MobShed[] composites;

    public OrMatcher(MobShed[] composites) {
        this.composites = composites;
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
        if(composites == null || composites.length <= 0) return BaseMatchResult.False;
        for(MobShed composite : composites) {
            BaseMatchResult result = composite.matches(entity, drop);

            // If we didn't match, go on to the next one:
            if(!result.isMatch()) continue;

            // Otherwise, just return it:
            return result;
        }

        return BaseMatchResult.False;
    }
}