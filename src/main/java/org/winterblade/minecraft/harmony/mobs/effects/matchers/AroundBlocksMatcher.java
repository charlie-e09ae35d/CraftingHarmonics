package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.blocks.BlockCountMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseNearbyBlockMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"aroundBlocks"})
@PrioritizedObject(priority = Priority.LOW)
public class AroundBlocksMatcher extends BaseNearbyBlockMatcher implements IEntityMatcher {
    public AroundBlocksMatcher(BlockCountMatcher matchers) {
        super(matchers.getWhat(), matchers.getDist(), matchers.getMin(), matchers.getMax());
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData metadata) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
