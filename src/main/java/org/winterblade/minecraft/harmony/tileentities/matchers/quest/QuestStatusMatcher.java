package org.winterblade.minecraft.harmony.tileentities.matchers.quest;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.quests.matchers.BaseQuestStatusMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"questingQuestStatus"})
@PrioritizedObject(priority = Priority.LOWER)
public class QuestStatusMatcher extends BaseQuestStatusMatcher implements ITileEntityMatcher {
    public QuestStatusMatcher(QuestStatusMatchData data) {
        super(data);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(null);
    }
}