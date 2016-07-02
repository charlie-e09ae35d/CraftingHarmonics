package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 7/1/2016.
 */
public abstract class BaseIsBurningMatcher {
    private final boolean shouldBeBurning;

    public BaseIsBurningMatcher(boolean shouldBeBurning) {
        this.shouldBeBurning = shouldBeBurning;
    }

    protected BaseMatchResult matches(Entity entity) {
        if(entity == null) return BaseMatchResult.False;
        return entity.isBurning() == shouldBeBurning
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }
}
