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
@Component(properties = {"not"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class NotMatcher implements IBlockDropMatcher {
    private final BlockDrop composite;

    public NotMatcher(BlockDrop composite) {
        this.composite = composite;
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
        // We aren't going to run any of the callbacks from here, as they're inverted.
        BaseDropMatchResult result = composite.matches(evt, drop);

        // Just invert the match:
        return new BaseDropMatchResult(!result.isMatch());
    }
}