package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseHeightMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"minHeight"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MinHeightMatcher extends BaseHeightMatcher implements ITileEntityMatcher {
    public MinHeightMatcher(int level) {super(level, Integer.MAX_VALUE);}

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        return matches(tileEntity.getPos());
    }
}