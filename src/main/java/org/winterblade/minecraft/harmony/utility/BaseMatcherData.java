package org.winterblade.minecraft.harmony.utility;

import org.winterblade.minecraft.harmony.api.Priority;

import javax.annotation.Nonnull;

/**
 * Created by Matt on 5/10/2016.
 */
public class BaseMatcherData <T> implements Comparable<BaseMatcherData> {
    private final T matcher;
    private final Priority priority;

    public BaseMatcherData(T matcher, Priority priority) {
        this.matcher = matcher;
        this.priority = priority;
    }

    @Override
    public int compareTo(@Nonnull BaseMatcherData o) {
        // Sort by priority, then by name.
        if(priority != o.priority) return priority.ordinal() - o.priority.ordinal();
        return matcher.getClass().getName().compareTo(o.matcher.getClass().getName());
    }

    public T getMatcher() {
        return matcher;
    }

    @Override
    public String toString() {
        return "[" + priority + ": " + matcher + "]";
    }
}

