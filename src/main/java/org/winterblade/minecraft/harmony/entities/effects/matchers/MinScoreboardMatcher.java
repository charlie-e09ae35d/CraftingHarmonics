package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseScoreboardMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"minScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinScoreboardMatcher extends BaseScoreboardMatcher implements IEntityMatcher {
    public MinScoreboardMatcher(ScoreboardMatchData score) {
        super(score.withMinValue());
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity.getEntityWorld(), entity);
    }
}