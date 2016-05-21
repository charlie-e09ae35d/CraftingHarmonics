package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockCountMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNearbyBlockMatcher;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"aroundBlocks"})
@PrioritizedObject(priority = Priority.LOW)
public class AroundBlocksMatcher extends BaseNearbyBlockMatcher implements IBlockDropMatcher {
    public AroundBlocksMatcher(BlockCountMatcher matchers) {
        super(matchers.getWhat(), matchers.getDist(), matchers.getMin(), matchers.getMax());
    }

    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getPos());
    }
}
