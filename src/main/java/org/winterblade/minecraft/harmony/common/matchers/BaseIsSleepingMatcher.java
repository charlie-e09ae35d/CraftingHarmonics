package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 5/30/2016.
 */
public abstract class BaseIsSleepingMatcher {
    private final boolean isSleeping;

    protected BaseIsSleepingMatcher(boolean isSleeping) {
        this.isSleeping = isSleeping;
    }

    protected BaseMatchResult matches(Entity entity) {
        if(EntityPlayerMP.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;

        return ((EntityPlayerMP)entity).isPlayerSleeping() ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
