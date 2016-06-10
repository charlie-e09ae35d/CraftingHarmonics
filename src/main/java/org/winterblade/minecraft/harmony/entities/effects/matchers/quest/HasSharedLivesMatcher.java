package org.winterblade.minecraft.harmony.entities.effects.matchers.quest;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.quests.matchers.BaseHasSharedLivesMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"questingHasSharedLives"})
@PrioritizedObject(priority = Priority.HIGH)
public class HasSharedLivesMatcher extends BaseHasSharedLivesMatcher implements IEntityMatcher {
    public HasSharedLivesMatcher(boolean hasSharedLives) {
        super(hasSharedLives);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity);
    }
}
