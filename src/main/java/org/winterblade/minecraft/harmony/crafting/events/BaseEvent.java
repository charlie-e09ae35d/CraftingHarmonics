package org.winterblade.minecraft.harmony.crafting.events;

import java.util.Random;

/**
 * Created by Matt on 4/13/2016.
 */
public abstract class BaseEvent {
    private final Random random;

    public BaseEvent(Random random) {
        this.random = random;
    }

    public Random getRandom() {
        return random;
    }
}
