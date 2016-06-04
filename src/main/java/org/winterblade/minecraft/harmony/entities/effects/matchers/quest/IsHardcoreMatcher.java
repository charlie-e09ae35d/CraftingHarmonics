package org.winterblade.minecraft.harmony.entities.effects.matchers.quest;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.quests.matchers.BaseIsHardcoreMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"questingIsHardcore"})
@PrioritizedObject(priority = Priority.HIGH)
public class IsHardcoreMatcher extends BaseIsHardcoreMatcher implements IEntityMatcher {
    public IsHardcoreMatcher(boolean isHardcore) {
        super(isHardcore);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, IEntityMatcherData iEntityMatcherData) {
        return matches();
    }
}