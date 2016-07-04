package org.winterblade.minecraft.harmony.entities;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.dto.Action;
import org.winterblade.minecraft.harmony.common.dto.NameClassPair;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.utility.ClassWalker;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Matt on 7/3/2016.
 */
public class EntityRegistry {
    private static final Map<UUID, EntityInteraction> handlers = new HashMap<>();
    private static final Set<UUID> activeHandlers = new LinkedHashSet<>();
    private static final LoadingCache<NameClassPair, Set<UUID>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build(new CacheLoader<NameClassPair, Set<UUID>>() {
                @Override
                public Set<UUID> load(NameClassPair key) throws Exception {
                    if(CraftingHarmonicsMod.getConfigManager().debugMobDropEvents()) {
                        LogHelper.info("Generating entity interaction handler cache for '{}'.", key.toString());
                    }

                    return activeHandlers.stream().filter(id -> {
                        EntityInteraction handler = handlers.get(id);
                        return handler != null && handler.isMatch(key);
                    }).collect(Collectors.toSet());
                }
            });
    private EntityRegistry() {}

    public static class EntityInteraction {
        private final List<String> what;
        private final EntityInteractionData[] interactions;
        private final boolean defaultDeny;

        public EntityInteraction(String[] what, EntityInteractionData[] interactions, boolean defaultDeny) {
            this.what = Arrays.asList(what);
            this.interactions = interactions;
            this.defaultDeny = defaultDeny;
        }

        public boolean check(Entity target, Entity source) {
            CallbackMetadata data = new BaseEntityMatcherData(source);

            for (EntityInteractionData interaction : interactions) {
                // Check our interaction, firing whatever we need to:
                Action result = interaction.check(target, source, data);

                // Check to see if we should continue or not:
                if(result != Action.PASS) return result == Action.ALLOW;
            }

            return !defaultDeny;
        }

        /**
         * Checks to see if this applies to the target entity
         * @param key    The name/class pair representing the entity
         * @return       True if it's applicable, false otherwise.
         */
        public boolean isMatch(NameClassPair key) {
            return what.contains(key.getName())
                    || !Collections.disjoint(what, ClassWalker.getAllClassNamesOf(key.getClazz()));
        }
    }

    public static class EntityInteractionData {
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

        private boolean isMatch(Entity target, Entity source, CallbackMetadata data) {
            List<Runnable> callbacks = new ArrayList<>();

            for (IEntityMatcher matcher : targetMatchers) {
                BaseMatchResult result = matcher.isMatch(target, data);
                if(!result.isMatch()) return false;
                callbacks.add(result.getCallback());
            }

            for (IEntityMatcher matcher : sourceMatchers) {
                BaseMatchResult result = matcher.isMatch(source, data);
                if(!result.isMatch()) return false;
                callbacks.add(result.getCallback());
            }

            // If we've got here, we can run our callbacks:
            for (Runnable callback : callbacks) {
                if(callback != null) callback.run();
            }

            return true;
        }
    }
}
