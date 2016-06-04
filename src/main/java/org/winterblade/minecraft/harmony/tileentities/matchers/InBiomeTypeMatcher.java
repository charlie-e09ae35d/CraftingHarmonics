package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseBiomeTypeMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"inBiomeTypes"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class InBiomeTypeMatcher extends BaseBiomeTypeMatcher implements ITileEntityMatcher {
    public InBiomeTypeMatcher(String[] biomeTypes) {super(biomeTypes);}

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, ITileEntityMatcherData iTileEntityMatcherData) {
        return matches(tileEntity.getWorld(), tileEntity.getPos());
    }
}