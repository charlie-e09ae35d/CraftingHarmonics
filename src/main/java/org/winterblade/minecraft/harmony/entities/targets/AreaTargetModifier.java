package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"area"})
public class AreaTargetModifier implements IEntityTargetModifier {
    private final double radius;

    public AreaTargetModifier(double radius) {
        this.radius = radius;
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        return getTargets(source.getEntityWorld(), source.getPosition(), Entity.class);
    }

    @Override
    public List<Entity> getTargets(TileEntity source, CallbackMetadata data) {
        return getTargets(source.getWorld(), source.getPos(), Entity.class);
    }

    protected <T extends Entity> List<Entity> getTargets(World world, BlockPos pos, Class<T> target) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos).expandXyz(radius);
        return world.getEntitiesWithinAABB(target, aabb);
    }
}
