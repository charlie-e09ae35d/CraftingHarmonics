package org.winterblade.minecraft.harmony.entities.callbacks;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "teleportAbsolute")
public class TeleportAbsoluteCallback extends TeleportBaseCallback {
    /*
     * Serialized properties
     */
    private double x;
    private double y;
    private double z;

    @Override
    protected void applyTeleport(Entity target, int targetDim) {
        teleport(target, targetDim, x, y, z);
    }
}
