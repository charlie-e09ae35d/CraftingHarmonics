package org.winterblade.minecraft.harmony.entities.targets;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseComponentDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/7/2016.
 */
@Component(properties = {"multitarget", "except"})
public class MultitargetTargetModifier implements IEntityTargetModifier {
    private final MultitargetContainer modifiers;
    private final MultitargetContainer except;

    public MultitargetTargetModifier(MultitargetContainer modifiers) {
        this(modifiers, new MultitargetContainer());
    }

    public MultitargetTargetModifier(MultitargetContainer modifiers, MultitargetContainer except) {
        this.modifiers = modifiers;
        this.except = except;
    }

    @Override
    public List<Entity> getTargets(Entity source, CallbackMetadata data) {
        if(modifiers == null || modifiers.getModifiers().length <= 0) return null;
        return getTargets(modifier -> modifier.getTargets(source, data));
    }

    @Override
    public List<Entity> getTargets(TileEntity source, CallbackMetadata data) {
        if(modifiers == null || modifiers.getModifiers().length <= 0) return null;
        return getTargets(modifier -> modifier.getTargets(source, data));
    }

    /**
     * Get the targets using a selector
     * @param selector    The selector to use
     * @return            The target list.
     */
    private List<Entity> getTargets(TargetSelector selector) {
        List<Entity> output = new ArrayList<>();

        for(IEntityTargetModifier modifier : modifiers.getModifiers()) {
            output.addAll(selector.getTargets(modifier));
        }

        for(IEntityTargetModifier modifier : except.getModifiers()) {
            output.addAll(selector.getTargets(modifier));
        }

        return output;
    }

    @FunctionalInterface
    private interface TargetSelector {
        List<Entity> getTargets(IEntityTargetModifier modifier);
    }

    public static class MultitargetContainer {
        private IEntityTargetModifier[] modifiers;

        public MultitargetContainer() {
            modifiers = new IEntityTargetModifier[0];
        }

        public IEntityTargetModifier[] getModifiers() {
            return modifiers;
        }

        @ScriptObjectDeserializer(deserializes = MultitargetContainer.class)
        public static class Deserializer extends BaseComponentDeserializer<MultitargetContainer, IEntityTargetModifier> {
            public Deserializer() {
                super(IEntityTargetModifier.class);
            }

            @Override
            protected MultitargetContainer newInstance(String type) {
                return new MultitargetContainer();
            }

            @Override
            protected void update(ScriptObjectMirror mirror, MultitargetContainer output, List<IEntityTargetModifier> iEntityTargetModifiers) {
                output.modifiers = new IEntityTargetModifier[iEntityTargetModifiers.size()];

                for (int i = 0; i < iEntityTargetModifiers.size(); i++) {
                    output.modifiers[i] = iEntityTargetModifiers.get(i);
                }
            }
        }
    }
}
