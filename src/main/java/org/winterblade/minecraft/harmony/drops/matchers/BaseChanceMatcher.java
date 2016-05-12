package org.winterblade.minecraft.harmony.drops.matchers;

import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

import java.util.Random;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseChanceMatcher {
    private final float chance;

    public BaseChanceMatcher(float chance) {
        this.chance = chance;
    }

    protected BaseDropMatchResult match(Random rand) {
        return new BaseDropMatchResult(rand.nextDouble() < chance);
    }
}
