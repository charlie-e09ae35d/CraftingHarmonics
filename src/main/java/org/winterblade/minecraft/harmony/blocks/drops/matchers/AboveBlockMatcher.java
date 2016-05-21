package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseAboveBlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/14/2016.
 */
@Component(properties = {"aboveBlock"})
@PrioritizedObject(priority = Priority.HIGH)
public class AboveBlockMatcher extends BaseAboveBlockMatcher implements IBlockDropMatcher {
    public AboveBlockMatcher(@Nullable BlockMatcher matcher) {
        super(matcher);
    }

    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getPos());
    }
}
