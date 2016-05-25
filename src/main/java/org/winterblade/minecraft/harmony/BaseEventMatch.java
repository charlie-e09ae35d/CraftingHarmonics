package org.winterblade.minecraft.harmony;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IMobMatcher;
import org.winterblade.minecraft.harmony.utility.BasePrioritizedData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by Matt on 5/20/2016.
 */
public class BaseEventMatch<TEvt, TResult, TMatcher extends IMobMatcher<TEvt, TResult>> {
    private final PriorityQueue<BasePrioritizedData<TMatcher>> matchers = new PriorityQueue<>();

    /**
     * Determine if the input event matches our target.
     * @param evt       The event to match
     * @param result    The result, which can be modified in the result callback.
     * @return          True if the matchers match.
     */
    public BaseMatchResult matches(TEvt evt, TResult result) {
        List<Runnable> callbacks = new ArrayList<>();

        // Iterate our matchers, finding the first one that fails.
        for(BasePrioritizedData<TMatcher> matcher : matchers) {
            BaseMatchResult matchResult = matcher.get().isMatch(evt, result);
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
        matchers.add(new BasePrioritizedData<>(matcher, priority));
    }

    public static abstract class BaseMatchHandler<T> {
        protected List<String> what;
        protected List<T> matchers;

        public void setWhat(String[] what) {
            this.what = Lists.newArrayList(what);
        }

        public void setMatchers(T[] matchers) {
            this.matchers = matchers != null ? Lists.newArrayList(matchers) : new ArrayList<>();
        }

        public boolean isMatch(String entity) {
            return what == null || what.size() <= 0 || what.contains(entity);
        }

        public List<T> getMatchers() {
            return matchers;
        }

        public abstract void apply(Random rand, EntityLivingBase entity);
    }

}
