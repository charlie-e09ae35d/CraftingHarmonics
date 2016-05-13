package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

/**
 * Created by Matt on 5/13/2016.
 */
public abstract class BaseHeightMatcher {
    private final int minHeight;
    private final int maxHeight;

    public BaseHeightMatcher(int minHeight, int maxHeight) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    protected BaseDropMatchResult matches(BlockPos pos) {
        return minHeight <= pos.getY() && pos.getY() <= maxHeight ? BaseDropMatchResult.True : BaseDropMatchResult.False;
    }
}
