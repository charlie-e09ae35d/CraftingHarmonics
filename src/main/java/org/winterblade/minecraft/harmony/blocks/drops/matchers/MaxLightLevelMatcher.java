package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.drops.matchers.BaseLightLevelMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"maxLightLevel"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxLightLevelMatcher extends BaseLightLevelMatcher implements IBlockDropMatcher {
    public MaxLightLevelMatcher(int max) {
        super(0,max);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(BlockEvent.HarvestDropsEvent evt, ItemStack drop) {
        return matches(evt.getWorld(), evt.getPos());
    }
}