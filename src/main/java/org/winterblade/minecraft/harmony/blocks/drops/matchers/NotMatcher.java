package org.winterblade.minecraft.harmony.blocks.drops.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNotMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

/**
 * Created by Matt on 5/13/2016.
 */
@Component(properties = {"not"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class NotMatcher extends BaseNotMatcher<BlockEvent.HarvestDropsEvent, ItemStack, IBlockDropMatcher, BlockDrop> implements IBlockDropMatcher {
    public NotMatcher(BlockDrop composite) {
        super(composite);
    }
}