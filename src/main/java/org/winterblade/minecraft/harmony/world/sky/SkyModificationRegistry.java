package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;

import javax.annotation.Nullable;
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
        } else if(stack.peek().equals(data)) {
            return;
        }

        // If we have it in the list, push it up to the top...
        if(stack.contains(data)) {
            stack.remove(data);
            stack.push(data);
        }

        stack.push(data);

        // We also need to update the player stacks:
        for (Map.Entry<UUID, Map<Integer, Deque<Data>>> dimEntry : playerStacks.entrySet()) {
            Deque<Data> playerStack = dimEntry.getValue().get(data.getTargetDim());
            if(playerStack == null) {
                playerStack = new LinkedList<>();
                dimEntry.getValue().put(data.getTargetDim(), playerStack);
            }

            playerStack.push(data);
        }

        // And sync it out to everybody...
        PacketHandler.wrapper.sendToAll(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()));
    }

    /**
     * Run the modification for the given entity, checking to make sure it's a player or not first
     * @param target            The entity to run it for
     * @param data              The data to transition to
     * @return                  True if the operation succeeded, false otherwise
     */
    public static boolean runModificationOn(Entity target, Data data) {
        Deque<Data> dimStack = getPlayerStackFor(target, data.getTargetDim(), true);

        // Or just this player...
        if(dimStack == null || dimStack.peek().equals(data)) {
            return false;
        }

        // If we have it in the list, push it up to the top...
        if(dimStack.contains(data)) {
            dimStack.remove(data);
            dimStack.push(data);
        }

        // Save our data and send it out.
        dimStack.push(data);
        PacketHandler.wrapper.sendTo(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()), (EntityPlayerMP) target);
        return true;
    }

    /**
     * Remove a modification from the global list.
     * @param data    The mod to remove
     */
    public static void removeModification(Data data) {
        // First, remove it from the global stack...
        Deque<Data> stack = globalStack.get(data.getTargetDim());
        if(stack != null) {
            stack.remove(data);
        }

        // Now, go through the active players and actually do the removal:
        for (EntityPlayerMP player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()) {
            removeModification(data, player);
        }
    }

    /**
     * Remove a modification from the given player
     * @param data      The mod to remove
     * @param target    The target player
     */
    public static void removeModification(Data data, Entity target) {
        Deque<Data> dimStack = getPlayerStackFor(target, data.getTargetDim(), false);

        // Make sure we have a stack...
        if(dimStack == null) return;

        // Check if it's the top of the stack...
        boolean isTop = dimStack.peek().equals(data);

        // If we're not removing anything, don't bother; also, if we're not the top entry, we can leave now..
        if(!dimStack.remove(data) || !isTop) return;

        // Alright, so we took the top off, now go ahead and get the new top of the stack...
        Data newData = dimStack.peek();

        // If we don't have a data, go ahead and send out a reset packet...
        if(newData == null) {
            newData = new Data(data.targetDim, 0, new SkyColorMapData[0], "");
        }

        PacketHandler.wrapper.sendTo(new SkyColorSync(newData.getTargetDim(), newData.getTransitionTime(), newData.getColormap()), (EntityPlayerMP) target);
    }

    /**
     * Gets the dimension stack for the given target
     * @param target       The entity to target
     * @param dimension    The dimension
     * @param create       If the maps should be created if they don't exist
     * @return             The stack, or null if the target is not an entity
     */
    @Nullable
    private static Deque<Data> getPlayerStackFor(Entity target, int dimension, boolean create) {
        // Or just this player...
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not setting the sky color for target ({}), as they aren't a player.", target.getName());
            return null;
        }

        Map<Integer, Deque<Data>> playerDimStack = playerStacks.get(target.getPersistentID());

        // If we don't have an entry for this...
        if (playerDimStack == null) {
            if(!create) return null;
            // Do we need to stash a new entry?
            playerDimStack = new HashMap<>();
            playerStacks.put(target.getPersistentID(), playerDimStack);
        }

        Deque<Data> dimStack = playerDimStack.get(dimension);

        if(dimStack == null) {
            if(!create) return null;
            dimStack = new LinkedList<>();
            playerDimStack.put(dimension, dimStack);
        }

        return dimStack;
    }

    /**
     * Sync the sky colors on login.
     * @param target    The target to sync to
     */
    public static void syncPlayerWithGlobal(EntityPlayerMP target) {
        // First, remove it from the global stack...
        UUID playerId = target.getPersistentID();

        // Clear our player stack...
        HashMap<Integer, Deque<Data>> playerMap = new HashMap<>();
        playerStacks.put(playerId, playerMap);

        for (Map.Entry<Integer, Deque<Data>> dimEntry : globalStack.entrySet()) {
            // Get our data from the top of the list
            Data data = dimEntry.getValue().peek();
            if(data == null) continue; // We don't need to sync an empty list...

            playerMap.put(dimEntry.getKey(), new LinkedList<>(dimEntry.getValue()));
            PacketHandler.wrapper.sendTo(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()), target);
        }
    }

    /**
     * Clean up the player's list after it's no longer necessary.
     * @param target    The target player.
     */
    public static void clearPlayer(EntityPlayerMP target) {
        playerStacks.remove(target.getPersistentID());
    }

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
