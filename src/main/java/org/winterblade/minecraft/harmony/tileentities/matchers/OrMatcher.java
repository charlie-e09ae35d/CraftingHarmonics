package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;
import org.winterblade.minecraft.harmony.common.matchers.BaseOrMatcher;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher extends BaseOrMatcher<TileEntity, ITileEntityMatcherData, ITileEntityMatcher, BaseTileEntityCallback> implements ITileEntityMatcher {
    public OrMatcher(BaseTileEntityCallback[] composites) {
        super(composites);
    }
}