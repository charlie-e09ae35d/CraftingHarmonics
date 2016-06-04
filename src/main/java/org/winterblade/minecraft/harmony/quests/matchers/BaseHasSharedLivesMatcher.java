package org.winterblade.minecraft.harmony.quests.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

/**
 * Created by Matt on 5/30/2016.
 */
public abstract class BaseHasSharedLivesMatcher {
    private final boolean hasSharedLives;

    protected BaseHasSharedLivesMatcher(boolean hasSharedLives) {
        this.hasSharedLives = hasSharedLives;
    }

    protected BaseMatchResult matches(Entity entity) {
        if(entity == null || EntityPlayerMP.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;
        return QuestRegistry.instance.hasSharedLives((EntityPlayerMP) entity) ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
