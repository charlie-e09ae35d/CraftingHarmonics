package org.winterblade.minecraft.harmony.quests.matchers;

import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

/**
 * Created by Matt on 5/30/2016.
 */
public abstract class BaseIsHardcoreMatcher {
    private final boolean isHardcore;

    protected BaseIsHardcoreMatcher(boolean isHardcore) {
        this.isHardcore = isHardcore;
    }

    protected BaseMatchResult matches() {
        return QuestRegistry.instance.isHardcoreModeEnabled() ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
