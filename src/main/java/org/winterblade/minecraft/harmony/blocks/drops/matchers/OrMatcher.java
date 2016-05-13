package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher implements IBlockDropMatcher {
    private final BlockDrop[] composites;

    public OrMatcher(BlockDrop[] composites) {
        this.composites = composites;
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
        if(composites == null || composites.length <= 0) return BaseDropMatchResult.False;
        for(BlockDrop composite : composites) {
            BaseDropMatchResult result = composite.matches(evt, drop);

            // If we didn't match, go on to the next one:
            if(!result.isMatch()) continue;

            // Otherwise, just return it:
            return result;
        }

        return BaseDropMatchResult.False;
    }
}