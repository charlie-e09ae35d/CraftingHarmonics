package org.winterblade.minecraft.harmony.tileentities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 6/4/2016.
 */
@TileEntityCallback(name = "explode")
public class ExplodeCallback extends CoordinateBaseCallback {
    /*
     * Serialized properties
     */
    private float strength;
    private boolean isSmoking;
    private ITileEntityCallback[] onComplete;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(strength < 0) throw new RuntimeException("Explosion strength cannot be less than zero.");
    }

    @Override
    protected void applyTo(TileEntity target, CallbackMetadata data) {
        BlockPos pos = getPosition(target);
        target.getWorld().createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), strength, isSmoking);
        runCallbacks(onComplete, target);
    }
}