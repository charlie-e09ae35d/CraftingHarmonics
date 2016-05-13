package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher implements IMobDropMatcher {
    private final MobDrop[] composites;

    public OrMatcher(MobDrop[] composites) {
        this.composites = composites;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param livingDropsEvent The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent livingDropsEvent, ItemStack drop) {
        if(composites == null || composites.length <= 0) return BaseDropMatchResult.False;
        for(MobDrop composite : composites) {
            BaseDropMatchResult result = composite.matches(livingDropsEvent, drop);

            // If we didn't match, go on to the next one:
            if(!result.isMatch()) continue;

            // Otherwise, just return it:
            return result;
        }

        return BaseDropMatchResult.False;
    }
}

