package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseAndMatcher;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"and"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AndMatcher extends BaseAndMatcher<TileEntity, ITileEntityMatcherData, ITileEntityMatcher, BaseTileEntityCallback> implements ITileEntityMatcher {
    public AndMatcher(BaseTileEntityCallback[] composites) {
        super(composites);
    }
}