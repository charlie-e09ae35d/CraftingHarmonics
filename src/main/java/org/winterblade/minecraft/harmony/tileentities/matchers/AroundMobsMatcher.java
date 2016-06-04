package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseNearbyMobMatcher;
import org.winterblade.minecraft.harmony.mobs.MobCountMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"aroundMobs"})
@PrioritizedObject(priority = Priority.LOWER)
public class AroundMobsMatcher extends BaseNearbyMobMatcher implements ITileEntityMatcher {
    public AroundMobsMatcher(MobCountMatcher matcher) {
        super(matcher.getWhat(), matcher.getDist(), matcher.getMin(), matcher.getMax());
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld(), tileEntity.getPos());
    }
}