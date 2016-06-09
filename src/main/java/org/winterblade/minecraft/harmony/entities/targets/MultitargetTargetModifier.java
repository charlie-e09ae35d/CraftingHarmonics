package org.winterblade.minecraft.harmony.entities.targets;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
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
        return getTargets(modifier -> modifier.getTargets(source, data), data);
    }

    @Override
    public List<Entity> getTargets(TileEntity source, CallbackMetadata data) {
        if(modifiers == null || modifiers.getModifiers().length <= 0) return null;
        return getTargets(modifier -> modifier.getTargets(source, data), data);
    }

    /**
     * Get the targets using a selector
     * @param selector    The selector to use
     * @return            The target list.
     */
    private List<Entity> getTargets(TargetSelector selector, CallbackMetadata data) {
        List<Entity> output = new ArrayList<>();

        for(IEntityTargetModifier modifier : modifiers.getModifiers()) {
            List<Entity> targets = selector.getTargets(modifier);
            for(Entity target : targets) {
                if(!entityMatches(target, modifiers.getMatchers(), data)) continue;
                output.add(target);
            }
        }

        for(IEntityTargetModifier modifier : except.getModifiers()) {
            List<Entity> targets = selector.getTargets(modifier);
            for(Entity target : targets) {
                if(!entityMatches(target, except.getMatchers(), data)) continue;
                output.remove(target);
            }
        }

        return output;
    }

    /**
     * Run through the matchers, and deal with their callbacks.
     * @param target       The target to run against
     * @param matchers     The matchers to run
     * @param data         The callback data to use
     * @return             True if the entity matches, false otherwise.
     */
    private boolean entityMatches(Entity target, IEntityMatcher[] matchers, CallbackMetadata data) {
        List<Runnable> callbacks = new ArrayList<>();
        for(IEntityMatcher matcher : matchers) {
            BaseMatchResult match = matcher.isMatch(target, data);
            if(!match.isMatch()) return false;
            callbacks.add(match.getCallback());
        }

        // Run our callbacks...
        for(Runnable callback : callbacks) {
            if(callback == null) continue;
            callback.run();
        }
        return true;
    }

    @FunctionalInterface
    private interface TargetSelector {
        List<Entity> getTargets(IEntityTargetModifier modifier);
    }

    public static class MultitargetContainer {
        private IEntityTargetModifier[] modifiers;
        private IEntityMatcher[] matchers;

        public MultitargetContainer() {
            modifiers = new IEntityTargetModifier[0];
        }

        public IEntityTargetModifier[] getModifiers() {
            return modifiers;
        }

        public IEntityMatcher[] getMatchers() {
            return matchers;
        }

        @ScriptObjectDeserializer(deserializes = MultitargetContainer.class)
        public static class Deserializer extends BaseMirroredDeserializer {
            @Override
            protected Object DeserializeMirror(ScriptObjectMirror mirror) {
                MultitargetContainer container = new MultitargetContainer();

                ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                        IEntityMatcher.class, IEntityTargetModifier.class}, mirror);

                // Check our target modifier first, because we have to have one...
                List<IEntityTargetModifier> modifiers = registry.getComponentsOf(IEntityTargetModifier.class);
                container.modifiers = modifiers.toArray(new IEntityTargetModifier[modifiers.size()]);

                // Now get our matchers, if any
                List<IEntityMatcher> matchers = registry.getComponentsOf(IEntityMatcher.class);
                container.matchers = matchers.toArray(new IEntityMatcher[matchers.size()]);

                return container;
            }
        }
    }
}
