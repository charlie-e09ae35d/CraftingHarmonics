package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"maxLooting"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxLootingMatcher implements IMobDropMatcher {
    private final int level;

    public MaxLootingMatcher(int level) {
        this.level = level;
    }


    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return new BaseMatchResult(evt.getLootingLevel() <= level);
    }
}
