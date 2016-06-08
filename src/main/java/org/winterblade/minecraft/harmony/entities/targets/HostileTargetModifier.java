package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"hostile"})
public class HostileTargetModifier extends AreaTargetModifier {
    public HostileTargetModifier(double radius) {
        super(radius);
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        return super.getTargets(source, data).stream()
                .filter(entity -> EntityMob.class.isAssignableFrom(entity.getClass()))
                .collect(Collectors.toList());
    }
}
