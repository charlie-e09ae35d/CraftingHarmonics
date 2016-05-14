package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/14/2016.
 */
public abstract class BaseAboveBlockMatcher {
    @Nullable
    private final BlockMatcher matcher;

    public BaseAboveBlockMatcher(@Nullable BlockMatcher matcher) {
        this.matcher = matcher;
    }

    protected final BaseDropMatchResult matches(World world, BlockPos pos) {
        return matcher != null && matcher.matches(world.getBlockState(pos.down()))
                ? BaseDropMatchResult.True
                : BaseDropMatchResult.False;
    }
}
