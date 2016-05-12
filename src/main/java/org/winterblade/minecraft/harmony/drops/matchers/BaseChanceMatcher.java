package org.winterblade.minecraft.harmony.drops.matchers;

import java.util.Random;

/**
 * Created by Matt on 5/11/2016.
 */
public class BaseChanceMatcher {
    private final float chance;

    public BaseChanceMatcher(float chance) {
        this.chance = chance;
    }

    protected boolean match(Random rand) {
        return rand.nextDouble() < chance;
    }
}
