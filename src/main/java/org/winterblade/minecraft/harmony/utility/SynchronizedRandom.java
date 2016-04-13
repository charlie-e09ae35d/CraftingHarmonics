package org.winterblade.minecraft.harmony.utility;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Matt on 4/13/2016.
 */
public class SynchronizedRandom {
    // For the server
    private static final Map<String, Random> randomMap = new HashMap<>();

    // For the client:
    private static Random myRandom;

    private SynchronizedRandom() {}

    public static void generateNewRandom(String playerId, long seed) {
        randomMap.put(playerId, new Random(seed));
    }

    public static Random getRandomFor(String playerId) {
        return randomMap.get(playerId);
    }

    public static Random getRandomFor(EntityPlayer craftingPlayer) {
        return getRandomFor(craftingPlayer.getUniqueID().toString());
    }

    public static void setMyRandom(long random) {
        myRandom = new Random(random);
    }

    public static Random getMyRandom() {
        return myRandom;
    }
}
