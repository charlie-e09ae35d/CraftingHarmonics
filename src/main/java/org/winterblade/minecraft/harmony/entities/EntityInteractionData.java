package org.winterblade.minecraft.harmony.entities;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.dto.Action;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 7/4/2016.
 */
public class EntityInteractionData {
    private final IEntityMatcher[] sourceMatchers;
    private final IEntityMatcher[] targetMatchers;
    private final IEntityCallback[] onAllowed;
    private final IEntityCallback[] onDenied;
    private final IEntityCallback[] onPassed;
    private final IEntityCallback[] onAllowedTarget;
    private final IEntityCallback[] onDeniedTarget;
    private final IEntityCallback[] onPassedTarget;
    private final Action matchedAction;
    private final Action unmatchedAction;

    public EntityInteractionData(IEntityMatcher[] sourceMatchers, IEntityMatcher[] targetMatchers,
                                 IEntityCallback[] onAllowed, IEntityCallback[] onDenied, IEntityCallback[] onPassed,
                                 IEntityCallback[] onAllowedTarget, IEntityCallback[] onDeniedTarget, IEntityCallback[] onPassedTarget,
                                 Action matchedAction, Action unmatchedAction) {
        this.sourceMatchers = sourceMatchers;
        this.targetMatchers = targetMatchers;
        this.onAllowed = onAllowed;
        this.onDenied = onDenied;
        this.onPassed = onPassed;
        this.onAllowedTarget = onAllowedTarget;
        this.onDeniedTarget = onDeniedTarget;
        this.onPassedTarget = onPassedTarget;
        this.matchedAction = matchedAction;
        this.unmatchedAction = unmatchedAction;
    }

    /**
     * Get the action result for this interaction
     *
     * @param target The target entity
     * @param source The source entity
     * @param data   The callback data to use
     * @return The action result (PASS/ALLOW/DENY)
     */
    public Action check(Entity target, Entity source, CallbackMetadata data) {
        boolean matches = isMatch(target, source, data);
        Action result = matches ? matchedAction : unmatchedAction;

        switch (result) {
            case PASS:
                MobTickRegistry.addCallbackSet(source, onPassed, data);
                MobTickRegistry.addCallbackSet(target, onPassedTarget, data);
                break;
            case ALLOW:
                MobTickRegistry.addCallbackSet(source, onAllowed, data);
                MobTickRegistry.addCallbackSet(target, onAllowedTarget, data);
                break;
            case DENY:
                MobTickRegistry.addCallbackSet(source, onDenied, data);
                MobTickRegistry.addCallbackSet(target, onDeniedTarget, data);
                break;
        }

        return result;
    }

    /**
     * Checks to see if our matchers match the source/target entities, and run their callbacks if they do.
     *
     * @param target The target entity
     * @param source The source entity
     * @param data   The callback data to use
     * @return True if matched, false otherwise.
     */
    private boolean isMatch(Entity target, Entity source, CallbackMetadata data) {
        List<Runnable> callbacks = new ArrayList<>();

        for (IEntityMatcher matcher : targetMatchers) {
            BaseMatchResult result = matcher.isMatch(target, data);
            if (!result.isMatch()) return false;
            callbacks.add(result.getCallback());
        }

        for (IEntityMatcher matcher : sourceMatchers) {
            BaseMatchResult result = matcher.isMatch(source, data);
            if (!result.isMatch()) return false;
            callbacks.add(result.getCallback());
        }

        // If we've got here, we can run our callbacks:
        for (Runnable callback : callbacks) {
            if (callback != null) callback.run();
        }

        return true;
    }

    @ScriptObjectDeserializer(deserializes = EntityInteractionData.class)
    public static class Deserializer extends BaseMirroredDeserializer {
        @Override
        protected Object DeserializeMirror(ScriptObjectMirror mirror) {
            // First, get our actions:
            Action matchedAction = getAction(mirror.get("matchedAction"));
            Action unmatchedAction = getAction(mirror.get("unmatchedAction"));

            // Now build out the data from each:
            InteractionTargetData targetData = InteractionTargetData.deserialize(mirror.get("target"));
            InteractionTargetData sourceData = InteractionTargetData.deserialize(mirror.get("source"));

            // Then build the actual object:
            return new EntityInteractionData(sourceData.getMatchers(), targetData.getMatchers(),
                    sourceData.getOnAllowed(), sourceData.getOnDenied(), sourceData.getOnPassed(),
                    targetData.getOnAllowed(), targetData.getOnDenied(), targetData.getOnPassed(),
                    matchedAction, unmatchedAction);
        }

        private Action getAction(Object matchedAction) {
            if(matchedAction == null) return Action.PASS;
            switch (matchedAction.toString().toLowerCase()) {
                case "allow":
                case "run":
                case "accept":
                case "permit":
                    return Action.ALLOW;
                case "deny":
                case "prevent":
                case "reject":
                case "refuse":
                    return Action.DENY;
                case "pass":
                default:
                    return Action.PASS;
            }
        }

    }

    private static class InteractionTargetData {
        private static final IScriptObjectDeserializer CALLBACK_DESERIALIZER = new BaseEntityCallback.Deserializer();

        private final IEntityMatcher[] matchers;
        private final IEntityCallback[] onAllowed;
        private final IEntityCallback[] onDenied;
        private final IEntityCallback[] onPassed;

        private InteractionTargetData(@Nullable IEntityMatcher[] matchers, @Nullable IEntityCallback[] onAllowed,
                                      @Nullable IEntityCallback[] onDenied, @Nullable IEntityCallback[] onPassed) {
            this.matchers = matchers != null ? matchers : new IEntityMatcher[0];
            this.onAllowed = onAllowed != null ? onAllowed : new IEntityCallback[0];
            this.onDenied = onDenied != null ? onDenied : new IEntityCallback[0];
            this.onPassed = onPassed != null ? onPassed : new IEntityCallback[0];
        }


        public static InteractionTargetData deserialize(Object input) {
            // Figure out if we have a valid input here:
            if(input == null || (!ScriptObjectMirror.class.isAssignableFrom(input.getClass()) &&
                    !ScriptObject.class.isAssignableFrom(input.getClass()))) return new InteractionTargetData(null, null, null, null);

            ScriptObjectMirror mirror;

            // Convert it over...
            if(ScriptObjectMirror.class.isAssignableFrom(input.getClass())) {
                mirror = (ScriptObjectMirror) input;
            } else {
                mirror = ScriptUtils.wrap((ScriptObject) input);
            }

            // Gather our callbacks:
            IEntityCallback[] onAllowed = mirror.containsKey("onAllowed")
                    ? DeserializerHelpers.convertArrayWithDeserializer(mirror, "onAllowed", CALLBACK_DESERIALIZER, IEntityCallback.class)
                    : new IEntityCallback[0];
            IEntityCallback[] onDenied = mirror.containsKey("onDenied")
                    ? DeserializerHelpers.convertArrayWithDeserializer(mirror, "onDenied", CALLBACK_DESERIALIZER, IEntityCallback.class)
                    : new IEntityCallback[0];
            IEntityCallback[] onPassed = mirror.containsKey("onPassed")
                    ? DeserializerHelpers.convertArrayWithDeserializer(mirror, "onPassed", CALLBACK_DESERIALIZER, IEntityCallback.class)
                    : new IEntityCallback[0];

            // Finally, get our matchers:
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{IEntityMatcher.class}, mirror);
            List<IEntityMatcher> components = registry.getComponentsOf(IEntityMatcher.class);

            // And build it out...
            return new InteractionTargetData(components != null ? components.toArray(new IEntityMatcher[components.size()]) : null,
                    onAllowed, onDenied, onPassed);
        }

        public IEntityMatcher[] getMatchers() {
            return matchers;
        }

        public IEntityCallback[] getOnAllowed() {
            return onAllowed;
        }

        public IEntityCallback[] getOnDenied() {
            return onDenied;
        }

        public IEntityCallback[] getOnPassed() {
            return onPassed;
        }
    }
}
