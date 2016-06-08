package org.winterblade.minecraft.harmony.entities.targets;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"multitarget", "except"})
public class MultitargetTargetModifier implements IEntityTargetModifier {
    private final IEntityTargetModifier[] modifiers;
    private final IEntityTargetModifier[] except;

    public MultitargetTargetModifier(IEntityTargetModifier[] modifiers) {
        this(modifiers, new IEntityTargetModifier[0]);
    }

    public MultitargetTargetModifier(IEntityTargetModifier[] modifiers, IEntityTargetModifier[] except) {
        this.modifiers = modifiers;
        this.except = except;
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        if(modifiers == null || modifiers.length <= 0) return null;
        List<Entity> output = new ArrayList<>();

        for(IEntityTargetModifier modifier : modifiers) {
            output.addAll(modifier.getTargets(source, data));
        }

        for(IEntityTargetModifier modifier : except) {
            output.removeAll(modifier.getTargets(source, data));
        }

        return output;
    }
}
