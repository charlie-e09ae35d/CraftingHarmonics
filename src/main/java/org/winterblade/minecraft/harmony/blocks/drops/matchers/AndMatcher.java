package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"and"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AndMatcher implements IBlockDropMatcher {
    private final BlockDrop[] composites;

    public AndMatcher(BlockDrop[] composites) {
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

        List<Runnable> runnables = new ArrayList<>();
        int callbacks = 0;

        for(BlockDrop composite : composites) {
            BaseDropMatchResult result = composite.matches(evt, drop);

            // If we didn't match, we failed:
            if(!result.isMatch()) return BaseDropMatchResult.False;

            // Add any callbacks we need to do:
            if(result.getCallback() != null) {
                runnables.add(result.getCallback());
                callbacks++;
            }
        }

        // If we can handle this easily, do so:
        if(callbacks <= 0) return BaseDropMatchResult.True;
        if(callbacks == 1) return new BaseDropMatchResult(true, runnables.get(0));

        // Return a composite function:
        return new BaseDropMatchResult(true, () -> {
            for(Runnable runnable : runnables) {
                runnable.run();
            }
        });
    }
}