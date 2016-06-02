package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;

import java.util.*;

/**
 * Created by Matt on 6/1/2016.
 */
public class SkyModificationRegistry {
    private SkyModificationRegistry() {}

    // These actually need to be stacks per dimension.
    private static final Map<Integer, Deque<Data>> globalStack = new HashMap<>();
    private static final Map<UUID, Map<Integer, Deque<Data>>> playerStacks = new HashMap<>();

    /**
     * Run a temporary modification of the sky colors for a dimension; reverting will go back to either the base from
     * an operation, or the provider default.
     * @param data    The data to transition to.
     */
    public static void runModification(Data data) {
        // If we've already applied it...
        Deque<Data> stack = globalStack.get(data.getTargetDim());
        if(stack == null) {
            stack = new LinkedList<>();
            globalStack.put(data.getTargetDim(), stack);
        } else if(stack.contains(data)) {
            return;
        }

        stack.push(data);
        PacketHandler.wrapper.sendToAll(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()));
    }

    /**
     * Run the modification for the given entity, checking to make sure it's a player or not first
     * @param target            The entity to run it for
     * @param data              The data to transition to
     * @return                  True if the operation succeeded, false otherwise
     */
    public static boolean runModificationOn(Entity target, Data data) {
        // Or just this player...
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not setting the sky color for target ({}), as they aren't a player.", target.getName());
            return false;
        }

        Map<Integer, Deque<Data>> playerDimStack = playerStacks.get(target.getPersistentID());

        // If we don't have an entry for this...
        if (playerDimStack == null) {
            // Do we need to stash a new entry?
            playerDimStack = new HashMap<>();
            playerStacks.put(target.getPersistentID(), playerDimStack);
        }

        Deque<Data> dimStack = playerDimStack.get(data.getTargetDim());

        if(dimStack == null) {
            dimStack = new LinkedList<>();
            globalStack.put(data.getTargetDim(), dimStack);
        } else if(dimStack.contains(data)) {
            return false;
        }

        // Save our data and send it out.
        dimStack.push(data);
        PacketHandler.wrapper.sendTo(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()), (EntityPlayerMP) target);
        return true;
    }

    // TODO: Reset functions.

    // TODO: On-login sync functions.

    public static class Data {
        private final int targetDim;
        private final int transitionTime;
        private final SkyColorMapData[] colormap;
        private final String hash;

        public Data(int targetDim, int transitionTime, SkyColorMapData[] colormap, String hash) {
            this.targetDim = targetDim;
            this.transitionTime = transitionTime;
            this.colormap = colormap;
            this.hash = hash;
        }

        public int getTargetDim() {
            return targetDim;
        }

        public int getTransitionTime() {
            return transitionTime;
        }

        public SkyColorMapData[] getColormap() {
            return colormap;
        }

        public String getHash() {
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            return getHash().equals(data.getHash());
        }

        @Override
        public int hashCode() {
            return getHash().hashCode();
        }
    }
}
