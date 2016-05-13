package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.drops.matchers.BaseChanceMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"chance"})
@PrioritizedObject(priority = Priority.HIGHEST)
public class ChanceMatcher extends BaseChanceMatcher implements IBlockDropMatcher {
    public ChanceMatcher(float chance) {super(chance);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param harvestDropsEvent The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return match(harvestDropsEvent.getWorld().rand);
    }
}
