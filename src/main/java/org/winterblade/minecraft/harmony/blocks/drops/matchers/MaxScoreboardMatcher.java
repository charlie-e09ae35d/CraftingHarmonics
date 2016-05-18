package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.drops.matchers.BaseScoreboardMatcher;
import org.winterblade.minecraft.harmony.dto.NameValuePair;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"maxScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxScoreboardMatcher extends BaseScoreboardMatcher implements IBlockDropMatcher {
    public MaxScoreboardMatcher(NameValuePair<Integer> score) {super(score.getName(), Integer.MIN_VALUE, score.getValue());}

    @Override
    public BaseDropMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getHarvester());
    }
}