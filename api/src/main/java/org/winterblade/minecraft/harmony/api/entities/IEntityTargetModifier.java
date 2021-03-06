package org.winterblade.minecraft.harmony.api.entities;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;

/**
 * Created by Matt on 5/24/2016.
 */
public interface IEntityTargetModifier {
    List<Entity> getTargets(Entity source, CallbackMetadata data);

    List<Entity> getTargets(TileEntity source, CallbackMetadata data);
}
