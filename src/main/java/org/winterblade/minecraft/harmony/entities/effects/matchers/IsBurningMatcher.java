package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsBurningMatcher;

/**
 * Created by Matt on 7/1/2016.
 */
@Component(properties = {"isBurning"})
@PrioritizedObject(priority = Priority.HIGHER)
public class IsBurningMatcher extends BaseIsBurningMatcher implements IEntityMatcher {
    public IsBurningMatcher(boolean shouldBeBurning) {
        super(shouldBeBurning);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity);
    }
}
