package org.winterblade.minecraft.harmony.entities.effects.matchers.quest;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.quests.matchers.BaseQuestStatusMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"questingQuestStatus"})
@PrioritizedObject(priority = Priority.LOWER)
public class QuestStatusMatcher extends BaseQuestStatusMatcher implements IEntityMatcher {
    public QuestStatusMatcher(QuestStatusMatchData data) {
        super(data);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata iEntityMatcherData) {
        return matches(entity);
    }
}