package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.WeakHashMap;

/**
 * Created by Matt on 5/22/2016.
 */
public abstract class BaseCooldownMatcher {
    private final WeakHashMap<Entity, Long> cooldowns = new WeakHashMap<>();

    private final int cooldownTicks;

    protected BaseCooldownMatcher(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    protected BaseMatchResult matches(Entity entity) {
        World inWorld = entity.getEntityWorld();

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
