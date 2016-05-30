package org.winterblade.minecraft.harmony.tileentities;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcherData;

import java.util.Random;

/**
 * Created by Matt on 5/29/2016.
 */
public class TileEntityEvent extends BaseEventMatch<TileEntity, ITileEntityMatcherData, ITileEntityMatcher> {

    public static class Handler extends BaseMatchHandler<TileEntityEvent,TileEntity> {
        @Override
        public void apply(Random rand, TileEntity entity) {

        }
    }
}
