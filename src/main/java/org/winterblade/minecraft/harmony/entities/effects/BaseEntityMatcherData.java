package org.winterblade.minecraft.harmony.entities.effects;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.utility.BasePrioritizedData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/24/2016.
 */
public class BaseEntityMatcherData implements IEntityMatcherData {
    public BaseMatchResult isMatch(Entity target, PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers) {
        List<Runnable> matcherCallbacks = new ArrayList<>();

        // Match us...
        IEntityMatcherData metadata = new BaseEntityMatcherData();
        for(BasePrioritizedData<IEntityMatcher> matcher : matchers) {
            BaseMatchResult matchResult = matcher.get().isMatch(target, metadata);
            if(!matchResult.isMatch()) return BaseMatchResult.False;
            if(matchResult.getCallback() != null) matcherCallbacks.add(matchResult.getCallback());
        }

        return new BaseMatchResult(true, () -> matcherCallbacks.forEach(Runnable::run));
    }

    public static BaseMatchResult match(Entity target, PriorityQueue<BasePrioritizedData<IEntityMatcher>> matchers) {
        return new BaseEntityMatcherData().isMatch(target, matchers);
    }
}
