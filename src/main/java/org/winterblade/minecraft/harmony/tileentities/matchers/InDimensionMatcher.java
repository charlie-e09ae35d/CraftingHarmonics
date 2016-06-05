package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseDimensionMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"inDimensions"})
@PrioritizedObject(priority = Priority.HIGHER)
public class InDimensionMatcher extends BaseDimensionMatcher implements ITileEntityMatcher {
    public InDimensionMatcher(Integer[] dimensionIds) {
        super(dimensionIds);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld());
    }
}
