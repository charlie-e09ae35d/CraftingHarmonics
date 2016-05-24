package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.common.matchers.BaseOrMatcher;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher extends BaseOrMatcher<BlockEvent.HarvestDropsEvent, ItemStack, IBlockDropMatcher, BlockDrop> implements IBlockDropMatcher {
    public OrMatcher(BlockDrop[] composites) {
        super(composites);
    }
}