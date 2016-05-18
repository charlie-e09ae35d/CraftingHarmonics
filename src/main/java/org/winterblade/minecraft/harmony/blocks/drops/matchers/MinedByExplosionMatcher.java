package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockDropRegistry;

import java.util.Map;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"minedByExplosion"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class MinedByExplosionMatcher implements IBlockDropMatcher {
    private final boolean byExplosion;

    public MinedByExplosionMatcher(boolean byExplosion) {
        this.byExplosion = byExplosion;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param harvestDropsEvent The event to match
     * @param drop              The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(BlockEvent.HarvestDropsEvent harvestDropsEvent, ItemStack drop) {
        return BlockDropRegistry.wasExploded(harvestDropsEvent.getPos()) == byExplosion ? BaseDropMatchResult.True : BaseDropMatchResult.False;
    }
}