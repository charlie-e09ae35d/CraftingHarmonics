package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

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

    protected BaseMatchResult matches(BlockPos pos) {
        return minHeight <= pos.getY() && pos.getY() <= maxHeight ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
