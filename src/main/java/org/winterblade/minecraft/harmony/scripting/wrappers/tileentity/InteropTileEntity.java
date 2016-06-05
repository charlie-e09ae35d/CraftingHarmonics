package org.winterblade.minecraft.harmony.scripting.wrappers.tileentity;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.ScriptInterop;

/**
 * Created by Matt on 6/4/2016.
 */
@ScriptInterop(wraps = TileEntity.class)
public class InteropTileEntity {
    private final TileEntity tileEntity;

    public InteropTileEntity(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    public String getName() {
        return tileEntity.getBlockType().getLocalizedName();
    }
}
