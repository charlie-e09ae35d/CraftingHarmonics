package org.winterblade.minecraft.harmony.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Matt on 4/13/2016.
 */
public class SynchronizedRandom {
    private static final Map<String, Random> randomMap = new HashMap<>();

    private SynchronizedRandom() {}

    public static void generateNewRandom(String playerId, long seed) {
        randomMap.put(playerId, new Random(seed));
    }

    public static Random getRandomFor(String playerId) {
        return randomMap.get(playerId);
    }
}
