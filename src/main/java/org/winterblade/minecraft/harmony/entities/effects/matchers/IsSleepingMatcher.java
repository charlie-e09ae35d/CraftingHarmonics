package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsSleepingMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"isSleeping"})
@PrioritizedObject(priority = Priority.HIGHER)
public class IsSleepingMatcher extends BaseIsSleepingMatcher implements IEntityMatcher {
    public IsSleepingMatcher(boolean isSleeping) {
        super(isSleeping);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity);
    }
}
