package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.WeakHashMap;

/**
 * Created by Matt on 5/22/2016.
 */
public abstract class BaseCooldownMatcher<T> {
    private final WeakHashMap<T, Long> cooldowns = new WeakHashMap<>();

    private final int cooldownTicks;

    protected BaseCooldownMatcher(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    protected BaseMatchResult matches(T entity, World inWorld) {
        // If we match, then go ahead and update our cooldown time...
        Runnable updateFn = () -> cooldowns.put(entity, inWorld.getTotalWorldTime()+cooldownTicks);

        // If we don't have an entry, it's a pass.
        if (!cooldowns.containsKey(entity)) {
            return new BaseMatchResult(true, updateFn);
        }

        // Check and see if we're at the point where we can run again, and do the thing if so.
        return cooldowns.get(entity) <= inWorld.getTotalWorldTime()
                ? new BaseMatchResult(true, updateFn)
                : BaseMatchResult.False;
    }

}
