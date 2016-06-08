package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"players"})
public class PlayerTargetModifier extends AreaTargetModifier {
    public PlayerTargetModifier(double radius) {
        super(radius);
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        return super.getTargets(source, data).stream()
                .filter(entity -> !EntityPlayer.class.isAssignableFrom(entity.getClass()))
                .collect(Collectors.toList());
    }
}