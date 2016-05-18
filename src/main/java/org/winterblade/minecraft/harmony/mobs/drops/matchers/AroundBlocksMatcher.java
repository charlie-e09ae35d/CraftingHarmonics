package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.blocks.BlockCountMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNearbyBlockMatcher;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"aroundBlocks"})
@PrioritizedObject(priority = Priority.LOW)
public class AroundBlocksMatcher extends BaseNearbyBlockMatcher implements IMobDropMatcher {
    public AroundBlocksMatcher(BlockCountMatcher matchers) {
        super(matchers.getWhat(), matchers.getDist(), matchers.getMin(), matchers.getMax());
    }

    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        return matches(evt.getEntity().getEntityWorld(), evt.getEntity().getPosition());
    }
}
