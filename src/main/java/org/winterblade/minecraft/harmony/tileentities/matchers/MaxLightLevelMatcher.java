package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseLightLevelMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"maxLightLevel"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxLightLevelMatcher extends BaseLightLevelMatcher implements ITileEntityMatcher {
    public MaxLightLevelMatcher(int max) {
        super(0,max);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        return matches(tileEntity.getWorld(), tileEntity.getPos());
    }
}