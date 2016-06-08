package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
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
        AxisAlignedBB aabb = new AxisAlignedBB(source.getPosition()).expandXyz(radius);
        return source.getEntityWorld().getEntitiesWithinAABB(Entity.class, aabb);
    }
}
