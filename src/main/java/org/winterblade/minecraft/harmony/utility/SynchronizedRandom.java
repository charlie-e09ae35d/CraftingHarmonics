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
        Random r = randomMap.get(playerId);

        if(r == null) {
            // This means we probably have a player that isn't logged in / is fake, etc
            // And we're probably safe to just generate a new random for them.
            System.err.println("Tried to get a stored random that didn't exist for '" + playerId + "'.");
            r = new Random();
            randomMap.put(playerId, r);
        }

        return r;
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
