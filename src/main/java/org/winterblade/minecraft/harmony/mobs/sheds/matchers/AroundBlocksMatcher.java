package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.blocks.BlockCountMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseNearbyBlockMatcher;

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
    public BaseMatchResult isMatch(EntityLivingBase entityLiving, ItemStack drop) {
        return matches(entityLiving.getEntityWorld(), entityLiving.getPosition());
    }
}
