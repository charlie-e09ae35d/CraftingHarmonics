package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.matchers.BaseBiomeTypeMatcher;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"inBiomeTypes"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class InBiomeTypeMatcher extends BaseBiomeTypeMatcher implements IBlockDropMatcher {
    public InBiomeTypeMatcher(String[] biomeTypes) {super(biomeTypes);}

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(BlockEvent.HarvestDropsEvent evt, ItemStack drop) {
        return matches(evt.getWorld(), evt.getPos());
    }
}