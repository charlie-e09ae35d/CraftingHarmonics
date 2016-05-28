package org.winterblade.minecraft.harmony.common.matchers;

import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;

/**
 * Basic NOT boolean matcher
 * @param <TEvt>          The event to match (ie: EntityLiving)
 * @param <TResult>       The result of the match (ie: ItemStack)
 * @param <TMatcher>      The matcher to use (ie: IEntityMatcher)
 * @param <TComposite>    The composite item (ie: MobPotionEffect)
 */
public abstract class BaseNotMatcher<TEvt, TResult, TMatcher extends IMobMatcher<TEvt,TResult>, TComposite extends BaseEventMatch<TEvt, TResult, TMatcher>> {
    private final TComposite composite;

    public BaseNotMatcher(TComposite composite) {
        this.composite = composite;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt       The event to match
     * @param result    The result; this can be modified.
     * @return          True if it should match; false otherwise
     */
    public final BaseMatchResult isMatch(TEvt evt, TResult result) {
        // We aren't going to run any of the callbacks from here, as they're inverted.
        BaseMatchResult matchResult = composite.matches(evt, result);

        // Just invert the match:
        return new BaseMatchResult(!matchResult.isMatch());
    }
}
