package org.winterblade.minecraft.harmony.mobs.drops;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;

import javax.annotation.Nonnull;
import java.util.PriorityQueue;

/**
 * Created by Matt on 5/4/2016.
 */
public class MobDrop {
    private ItemStack what;
    private int min;
    private int max;
    private double lootingMultiplier;
    private final PriorityQueue<MobDropMatcherData> matchers = new PriorityQueue<>();

    public ItemStack getWhat() {
        return what;
    }

    public int getMin() {
        return min < 0 ? 0 : min;
    }

    public int getMax() {
        return max < 0 ? 0 : max;
    }

    public double getLootingMultiplier() {
        return lootingMultiplier;
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

    public void setLootingMultiplier(Double lootingMultiplier) {
        this.lootingMultiplier = lootingMultiplier;
    }

    /**
     * Determine if the input event matches our target.
     * @param evt       The event to match
     * @param drop      The dropped item; this can be modified.
     * @return          True if the matchers match.
     */
    public boolean matches(LivingDropsEvent evt, ItemStack drop) {

        // Iterate our matchers, finding the first one that fails.
        for(MobDropMatcherData matcher : matchers) {
            if(!matcher.getMatcher().isMatch(evt, drop)) return false;
        }

        return true;
    }

    /**
     * Add a matcher to the matcher list
     * @param matcher     The matcher to add
     * @param priority    The priority to add it at
     */
    public void addMatcher(IMobDropMatcher matcher, Priority priority) {
        matchers.add(new MobDropMatcherData(matcher, priority));
    }

    private class MobDropMatcherData implements Comparable<MobDropMatcherData> {
        private final IMobDropMatcher matcher;
        private final Priority priority;

        MobDropMatcherData (IMobDropMatcher matcher, Priority priority) {
            this.matcher = matcher;
            this.priority = priority;
        }

        @Override
        public int compareTo(@Nonnull MobDropMatcherData o) {
            // Sort by priority, then by name.
            if(priority != o.priority) return priority.ordinal() - o.priority.ordinal();
            return matcher.getClass().getName().compareTo(o.matcher.getClass().getName());
        }

        public IMobDropMatcher getMatcher() {
            return matcher;
        }

        @Override
        public String toString() {
            return "[" + priority + ": " + matcher + "]";
        }
    }
}
