package org.winterblade.minecraft.harmony.utility;

import org.winterblade.minecraft.harmony.api.Priority;

import javax.annotation.Nonnull;

/**
 * Created by Matt on 5/10/2016.
 */
public class BasePrioritizedData<T> implements Comparable<BasePrioritizedData> {
    private final T matcher;
    private final Priority priority;

    public BasePrioritizedData(T matcher, Priority priority) {
        this.matcher = matcher;
        this.priority = priority;
    }

    @Override
    public int compareTo(@Nonnull BasePrioritizedData o) {
        // Sort by priority, then by name.
        if(priority != o.priority) return priority.compareTo(o.priority) < 0 ? 1 : -1;
        return matcher.getClass().getName().compareTo(o.matcher.getClass().getName());
    }

    public T get() {
        return matcher;
    }

    @Override
    public String toString() {
        return "[" + priority + ": " + matcher + "]";
    }
}

