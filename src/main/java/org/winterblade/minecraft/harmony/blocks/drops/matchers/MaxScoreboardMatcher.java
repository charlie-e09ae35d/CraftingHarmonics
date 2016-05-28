package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseScoreboardMatcher;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"maxScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxScoreboardMatcher extends BaseScoreboardMatcher implements IBlockDropMatcher {
    public MaxScoreboardMatcher(ScoreboardMatchData score) {
        super(score.getName(), Integer.MIN_VALUE, score.getValue(), score.getPlayer());
    }

    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return matches(harvestDropsEvent.getWorld(), harvestDropsEvent.getHarvester());
    }
}