package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseIsStormingMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"isStorming"})
@PrioritizedObject(priority = Priority.HIGHER)
public class IsStormingMatcher extends BaseIsStormingMatcher implements ITileEntityMatcher {
    public IsStormingMatcher(WeatherMatcher matcher) {
        super(matcher);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld(), tileEntity.getPos());
    }
}