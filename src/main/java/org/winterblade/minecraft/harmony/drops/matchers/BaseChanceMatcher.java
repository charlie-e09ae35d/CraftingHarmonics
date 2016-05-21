package org.winterblade.minecraft.harmony.drops.matchers;

import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.Random;

/**
 * Created by Matt on 5/11/2016.
 */
public abstract class BaseChanceMatcher {
    private final float chance;

    public BaseChanceMatcher(float chance) {
        this.chance = chance;
    }

    protected BaseMatchResult match(Random rand) {
        return new BaseMatchResult(rand.nextDouble() < chance);
    }
}
