package org.winterblade.minecraft.harmony.mobs.drops.matchers.quest;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.PlayerMatcherMode;
import org.winterblade.minecraft.harmony.quests.matchers.BaseQuestStatusMatcher;

/**
 * Created by Matt on 5/30/2016.
 */
@Component(properties = {"questingQuestStatus"})
@PrioritizedObject(priority = Priority.LOWER)
public class QuestStatusMatcher extends BaseQuestStatusMatcher implements IMobDropMatcher {
    public QuestStatusMatcher(QuestStatusMatchData data) {
        super(data);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param event The event to match
     * @param drop  The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(LivingDropsEvent event, ItemStack drop) {
        if(event.getSource() == null && data.getMode() == PlayerMatcherMode.CURRENT) return BaseMatchResult.False;
        return matches(event.getSource() != null ? event.getSource().getEntity() : null);
    }
}