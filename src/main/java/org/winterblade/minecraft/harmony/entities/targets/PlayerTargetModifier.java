package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.List;

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
        return getTargets(source.getEntityWorld(), source.getPosition(), EntityPlayer.class);
    }

    @Override
    public List<Entity> getTargets(TileEntity source, CallbackMetadata data) {
        return getTargets(source.getWorld(), source.getPos(), EntityPlayer.class);
    }
}