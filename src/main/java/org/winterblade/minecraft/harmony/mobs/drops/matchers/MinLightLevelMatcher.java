package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseLightLevelMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"minLightLevel"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinLightLevelMatcher extends BaseLightLevelMatcher implements IMobDropMatcher {
    public MinLightLevelMatcher(int min) {
        super(min,15);
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
        return matches(livingDropsEvent.getEntity());
    }
}
