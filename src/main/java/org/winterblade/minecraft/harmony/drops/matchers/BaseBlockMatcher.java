package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseBlockMatcher {
    @Nullable
    private final BlockMatcher matcher;

    public BaseBlockMatcher(@Nullable BlockMatcher matcher) {
        this.matcher = matcher;
    }

    protected BaseDropMatchResult matches(World world, BlockPos pos) {
        return matcher != null && matcher.matches(world.getBlockState(pos))
                ? BaseDropMatchResult.True
                : BaseDropMatchResult.False;
    }

}
