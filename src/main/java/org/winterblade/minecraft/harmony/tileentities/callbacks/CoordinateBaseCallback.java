package org.winterblade.minecraft.harmony.tileentities.callbacks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

/**
 * Created by Matt on 6/4/2016.
 */
public class CoordinateBaseCallback extends BaseTileEntityCallback {
    /*
     * Serialized properties
     */

    // Offset coords:
    protected int x;
    protected int y;
    protected int z;

    protected BlockPos getPosition(TileEntity target) {
        return target.getPos().add(x, y, z);
    }
}
