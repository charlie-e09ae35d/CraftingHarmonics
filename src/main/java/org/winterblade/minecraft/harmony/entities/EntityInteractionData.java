package org.winterblade.minecraft.harmony.entities;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.dto.Action;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

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
}
