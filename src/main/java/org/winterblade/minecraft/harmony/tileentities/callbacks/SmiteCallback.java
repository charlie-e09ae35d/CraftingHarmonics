package org.winterblade.minecraft.harmony.tileentities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

/**
 * Created by Matt on 6/4/2016.
 */
@TileEntityCallback(name = "smite")
public class SmiteCallback extends CoordinateBaseCallback {
    private boolean effectOnly;
    private ITileEntityCallback[] onComplete;

    /**
     * Apply an action to the target.
     *
     * @param target The target to apply to.
     * @param data   Any event data to deal with.
     */
    @Override
    protected void applyTo(TileEntity target, BaseTileEntityCallback.Data data) {
        BlockPos pos = getPosition(target);
        EntityLightningBolt bolt = new EntityLightningBolt(target.getWorld(), pos.getX(), pos.getY(), pos.getZ(), effectOnly);
        target.getWorld().addWeatherEffect(bolt);
        runCallbacks(onComplete, target);
    }


    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {

    }
}
