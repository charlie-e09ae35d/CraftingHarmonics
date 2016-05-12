package org.winterblade.minecraft.harmony.drops;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.drops.IBaseDropMatcher;
import org.winterblade.minecraft.harmony.utility.BaseMatcherData;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/10/2016.
 */
public abstract class BaseDrop<TEvt, TMatch extends IBaseDropMatcher<TEvt>> {
    private final PriorityQueue<BaseMatcherData<TMatch>> matchers = new PriorityQueue<>();
    private ItemStack what;
    private int min;
    private int max;

    public ItemStack getWhat() {
        return what;
    }

    public int getMin() {
        return min < 0 ? 0 : min;
    }

    public int getMax() {
        return max < 0 ? 0 : max;
    }

    public void setWhat(ItemStack what) {
        this.what = what;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Determine if the input event matches our target.
     * @param evt       The event to match
     * @param drop      The dropped item; this can be modified.
     * @return          True if the matchers match.
     */
    public BaseDropMatchResult matches(TEvt evt, ItemStack drop) {
        List<Runnable> callbacks = new ArrayList<>();

        // Iterate our matchers, finding the first one that fails.
        for(BaseMatcherData<TMatch> matcher : matchers) {
            BaseDropMatchResult result = matcher.getMatcher().isMatch(evt, drop);
            if(!result.isMatch()) return BaseDropMatchResult.False;
            if(result.getCallback() != null) callbacks.add(result.getCallback());
        }

        // Return a composite of all our runnables...
        return new BaseDropMatchResult(true, () -> {
            callbacks.forEach(Runnable::run);
        });
    }

    /**
     * Add a matcher to the matcher list
     * @param matcher     The matcher to add
     * @param priority    The priority to add it at
     */
    public void addMatcher(TMatch matcher, Priority priority) {
        matchers.add(new BaseMatcherData<>(matcher, priority));
    }
}
