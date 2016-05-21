package org.winterblade.minecraft.harmony.drops.matchers;

import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;

/**
 * Basic OR boolean matcher
 * @param <TEvt>          The event to match (ie: EntityLiving)
 * @param <TResult>       The result of the match (ie: ItemStack)
 * @param <TMatcher>      The matcher to use (ie: IMobPotionEffectMatcher)
 * @param <TComposite>    The composite item (ie: MobPotionEffect)
 */
public abstract class BaseOrMatcher <TEvt, TResult, TMatcher extends IMobMatcher<TEvt,TResult>, TComposite extends BaseEventMatch<TEvt, TResult, TMatcher>> {
    private final TComposite[] composites;

    public BaseOrMatcher(TComposite[] composites) {
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
        for(TComposite composite : composites) {
            BaseMatchResult result = composite.matches(entity, drop);

            // If we didn't match, go on to the next one:
            if(!result.isMatch()) continue;

            // Otherwise, just return it:
            return result;
        }

        return BaseMatchResult.False;
    }
}
