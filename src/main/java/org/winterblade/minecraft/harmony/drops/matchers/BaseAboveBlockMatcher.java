package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.blocks.BlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/14/2016.
 */
public abstract class BaseAboveBlockMatcher extends BaseBlockMatcher {
    @Nullable
    public BaseAboveBlockMatcher(@Nullable BlockMatcher matcher) {
        super(matcher);
    }

    protected BaseDropMatchResult matches(World world, BlockPos pos) {
        return super.matches(world, pos.down());
    }
}
