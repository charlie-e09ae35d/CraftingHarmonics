package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"animals"})
public class AnimalTargetModifier extends AreaTargetModifier {
    public AnimalTargetModifier(double radius) {
        super(radius);
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        return getTargets(source, EntityAnimal.class);
    }
}