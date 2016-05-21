package org.winterblade.minecraft.harmony;

import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;
import org.winterblade.minecraft.harmony.utility.BaseMatcherData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/20/2016.
 */
public class BaseEventMatch<TEvt, TResult, TMatcher extends IMobMatcher<TEvt, TResult>> {
    private final PriorityQueue<BaseMatcherData<TMatcher>> matchers = new PriorityQueue<>();

    /**
     * Determine if the input event matches our target.
     * @param evt       The event to match
     * @param result    The result, which can be modified in the result callback.
     * @return          True if the matchers match.
     */
    public BaseMatchResult matches(TEvt evt, TResult result) {
        List<Runnable> callbacks = new ArrayList<>();

        // Iterate our matchers, finding the first one that fails.
        for(BaseMatcherData<TMatcher> matcher : matchers) {
            BaseMatchResult matchResult = matcher.getMatcher().isMatch(evt, result);
            if(!matchResult.isMatch()) return BaseMatchResult.False;
            if(matchResult.getCallback() != null) callbacks.add(matchResult.getCallback());
        }

        // Return a composite of all our runnables...
        return new BaseMatchResult(true, () -> callbacks.forEach(Runnable::run));
    }

    /**
     * Add a matcher to the matcher list
     * @param matcher     The matcher to add
     * @param priority    The priority to add it at
     */
    public void addMatcher(TMatcher matcher, Priority priority) {
        matchers.add(new BaseMatcherData<>(matcher, priority));
    }
}
