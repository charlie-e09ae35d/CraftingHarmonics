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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"and"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AndMatcher implements IMobShedMatcher {
    private final MobShed[] composites;

    public AndMatcher(MobShed[] composites) {
        this.composites = composites;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        if(composites == null || composites.length <= 0) return BaseMatchResult.False;

        List<Runnable> runnables = new ArrayList<>();
        int callbacks = 0;

        for(MobShed composite : composites) {
            BaseMatchResult result = composite.matches(entity, drop);

            // If we didn't match, we failed:
            if(!result.isMatch()) return BaseMatchResult.False;

            // Add any callbacks we need to do:
            if(result.getCallback() != null) {
                runnables.add(result.getCallback());
                callbacks++;
            }
        }

        // If we can handle this easily, do so:
        if(callbacks <= 0) return BaseMatchResult.True;
        if(callbacks == 1) return new BaseMatchResult(true, runnables.get(0));

        // Return a composite function:
        return new BaseMatchResult(true, () -> {
            for(Runnable runnable : runnables) {
                runnable.run();
            }
        });
    }
}