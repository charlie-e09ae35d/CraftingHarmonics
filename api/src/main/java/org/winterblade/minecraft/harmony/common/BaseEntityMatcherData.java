package org.winterblade.minecraft.harmony.common;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.BasePrioritizedData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/24/2016.
 */
public class BaseEntityMatcherData extends CallbackMetadata {
    public BaseEntityMatcherData(Entity source) {
        super(source);
    }

    public BaseMatchResult isMatch(Entity target, PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers) {
        List<Runnable> matcherCallbacks = new ArrayList<>();

        // Match us...
        CallbackMetadata metadata = new BaseEntityMatcherData(target);
        for(BasePrioritizedData<IEntityMatcher> matcher : matchers) {
            BaseMatchResult matchResult = matcher.get().isMatch(target, metadata);
            if(!matchResult.isMatch()) return BaseMatchResult.False;
            if(matchResult.getCallback() != null) matcherCallbacks.add(matchResult.getCallback());
        }

        return new BaseMatchResult(true, () -> matcherCallbacks.forEach(Runnable::run));
    }

    public static BaseMatchResult match(Entity target, PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers) {
        return new BaseEntityMatcherData(target).isMatch(target, matchers);
    }
}
