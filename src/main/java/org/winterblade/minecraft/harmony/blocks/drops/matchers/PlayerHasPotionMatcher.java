package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.drops.matchers.BaseHasPotionMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"playerHasPotionEffect"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class PlayerHasPotionMatcher extends BaseHasPotionMatcher implements IBlockDropMatcher {
    public PlayerHasPotionMatcher(Potion potion) {
        super(potion);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent evt, ItemStack drop) {
        return matches(evt.getHarvester());
    }
}

