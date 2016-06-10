package org.winterblade.minecraft.harmony.api.tileentities;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * The actual callback for a method; needs to be annotated with @TileEntityCallback.  See {@link TileEntityCallback} for more
 * information on what you need to do.
 */
public interface ITileEntityCallback {
    void apply(TileEntity target, CallbackMetadata metadata);
}
