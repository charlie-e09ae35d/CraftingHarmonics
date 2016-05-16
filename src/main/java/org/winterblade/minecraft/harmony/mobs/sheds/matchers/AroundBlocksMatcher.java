package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.blocks.BlockCountMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNearbyBlockMatcher;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"aroundBlocks"})
@PrioritizedObject(priority = Priority.LOW)
public class AroundBlocksMatcher extends BaseNearbyBlockMatcher implements IMobShedMatcher {
    public AroundBlocksMatcher(BlockCountMatcher matchers) {
        super(matchers.getWhat(), matchers.getDist(), matchers.getMin(), matchers.getMax());
    }

    @Override
    public BaseDropMatchResult isMatch(EntityLiving entityLiving, ItemStack drop) {
        return matches(entityLiving.getEntityWorld(), entityLiving.getPosition());
    }
}
