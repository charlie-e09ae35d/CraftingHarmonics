package org.winterblade.minecraft.harmony.drops.matchers;

import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic AND boolean matcher
 * @param <TEvt>          The event to match (ie: EntityLiving)
 * @param <TResult>       The result of the match (ie: ItemStack)
 * @param <TMatcher>      The matcher to use (ie: IMobPotionEffectMatcher)
 * @param <TComposite>    The composite item (ie: MobPotionEffect)
 */
public abstract class BaseAndMatcher <TEvt, TResult, TMatcher extends IMobMatcher<TEvt,TResult>, TComposite extends BaseEventMatch<TEvt, TResult, TMatcher>> {
    private final TComposite[] composites;

    public BaseAndMatcher(TComposite[] composites) {
        this.composites = composites;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param drop             The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    public final BaseMatchResult isMatch(TEvt entity, TResult drop) {
        if(composites == null || composites.length <= 0) return BaseMatchResult.False;

        List<Runnable> runnables = new ArrayList<>();
        int callbacks = 0;

        for(TComposite composite : composites) {
            BaseMatchResult result = composite.matches(entity, drop);

            // If we didn't match, we failed:
            if(!result.isMatch()) return BaseMatchResult.False;

            // Add any callbacks we need to do:
            if(result.getCallback() != null) {
                runnables.add(result.getCallback());
                callbacks++;
            }
        }

        // If we can handle this easily, do so:
        if(callbacks <= 0) return BaseMatchResult.True;
        if(callbacks == 1) return new BaseMatchResult(true, runnables.get(0));

        // Return a composite function:
        return new BaseMatchResult(true, () -> {
            for(Runnable runnable : runnables) {
                runnable.run();
            }
        });
    }
}
