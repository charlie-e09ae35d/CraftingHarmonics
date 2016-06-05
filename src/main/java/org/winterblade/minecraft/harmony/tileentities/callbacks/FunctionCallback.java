package org.winterblade.minecraft.harmony.tileentities.callbacks;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.scripting.wrappers.tileentity.InteropTileEntity;
import org.winterblade.minecraft.harmony.scripting.wrappers.world.InteropWorld;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

/**
 * Created by Matt on 6/4/2016.
 */
@TileEntityCallback(name = "function")
public class FunctionCallback extends BaseTileEntityCallback {
    private JSCallback callback;

    public FunctionCallback() {}

    public FunctionCallback(JSCallback callback) {
        this.callback = callback;
    }

    @Override
    public void applyTo(TileEntity target, Data data) {
        callback.apply(new InteropTileEntity(target), new InteropWorld(target.getWorld()));
    }

    @FunctionalInterface
    public static interface JSCallback {
        void apply(InteropTileEntity entity, InteropWorld world);
    }

}
