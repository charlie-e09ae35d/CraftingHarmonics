package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"target"})
public class DirectTargetModifier implements IEntityTargetModifier {
    private final String target;

    public DirectTargetModifier(String target) {
        this.target = target.toLowerCase();
    }
    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        switch (target) {
            case "original":
                return Collections.singletonList(data.getSourceAs(Entity.class));
        }
        return null;
    }
}
