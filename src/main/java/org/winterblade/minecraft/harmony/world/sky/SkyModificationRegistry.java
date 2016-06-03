package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/1/2016.
 */
public class SkyModificationRegistry {
    private SkyModificationRegistry() {}

    // These actually need to be stacks per dimension.
    private static final Map<UUID, Map<Integer, Deque<Data>>> playerStacks = new HashMap<>();

    /**
     * Run a temporary modification of the sky colors for a dimension; reverting will go back to either the base from
     * an operation, or the provider default.
     * @param data    The data to transition to.
     */
    public static void runModification(Data data) {
        // Get every UUID we need to apply this to...
        Set<UUID> uuids = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()
                .stream().map(EntityPlayerMP::getPersistentID).collect(Collectors.toSet());
        uuids.addAll(playerStacks.keySet());

        // We then need to update the players
        for (UUID uuid : uuids) {
            runModificationOn(uuid, data);
        }

        // Now send out the updated data...
        PacketHandler.wrapper.sendToAll(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()));
    }

    /**
     * Run the modification for the given entity, checking to make sure it's a player or not first
     * @param target            The entity to run it for
     * @param data              The data to transition to
     * @return                  True if the operation succeeded, false otherwise
     */
    public static boolean runModificationOn(Entity target, Data data) {
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not setting the sky color for target ({}), as they aren't a player.", target.getName());
            return false;
        }

        if(!runModificationOn(target.getPersistentID(), data)) return false;

        PacketHandler.wrapper.sendTo(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()), (EntityPlayerMP) target);
        return true;
    }

    /**
     * Remove a modification from the global list.
     * @param data    The mod to remove
     */
    public static void removeModification(Data data) {
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
        if(!EntityPlayerMP.class.isAssignableFrom(target.getClass())) {
            LogHelper.warn("Not removing the sky color for target ({}), as they aren't a player.", target.getName());
            return;
        }

        Deque<Data> dimStack = getPlayerStackFor(target.getPersistentID(), data.getTargetDim(), false);

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
     * Remove the top sky modifications
     * @param targetDim    The dimension to modify
     */
    public static void removeModifications(int targetDim) {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList();

        for (EntityPlayerMP player : players) {
            // First, get their map entry...
            Map<Integer, Deque<Data>> playerStack = playerStacks.get(player.getPersistentID());
            if(playerStack == null) continue;

            // Then get the stack for the actual dimension...
            Deque<Data> dimStack = getPlayerStackFor(player.getPersistentID(), targetDim, false);
            if(dimStack == null) continue;

            // Finally, check our data...
            Data data = dimStack.peek();
            if(data == null) continue;

            // Aaaand remove it.
            removeModification(data,player);
        }
    }

    public static boolean removeModifications(int targetDim, Entity target) {
        Deque<Data> stack = getPlayerStackFor(target.getPersistentID(), targetDim, false);
        if(stack == null) return false;
        removeModification(stack.peek(), target);
        return true;
    }

    /**
     * Sync the sky colors on login.
     * @param target    The target to sync to
     */
    public static void resyncPlayerData(EntityPlayerMP target) {
        Map<Integer, Deque<Data>> dimStacks = playerStacks.get(target.getPersistentID());

        // If we don't have any...
        if(dimStacks == null) return;

        for (Deque<Data> dimStack : dimStacks.values()) {
            Data data = dimStack.peek();
            if(data == null) continue;
            PacketHandler.wrapper.sendTo(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()), target);
        }
    }

    /**
     * Gets the dimension stack for the given target
     * @param target       The entity to target
     * @param dimension    The dimension
     * @param create       If the maps should be created if they don't exist
     * @return             The stack, or null if the target is not an entity
     */
    @Nullable
    private static Deque<Data> getPlayerStackFor(UUID target, int dimension, boolean create) {
        // Or just this player...
        Map<Integer, Deque<Data>> playerDimStack = playerStacks.get(target);

        // If we don't have an entry for this...
        if (playerDimStack == null) {
            if(!create) return null;
            // Do we need to stash a new entry?
            playerDimStack = new HashMap<>();
            playerStacks.put(target, playerDimStack);
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
     * Run the modification for the given UUID
     * @param target            The UUID to run it for
     * @param data              The data to transition to
     * @return                  True if the operation succeeded, false otherwise
     */
    private static boolean runModificationOn(UUID target, Data data) {
        Deque<Data> dimStack = getPlayerStackFor(target, data.getTargetDim(), true);

        if(dimStack == null) {
            return false;
        }

        // Check if this is the top or not...
        Data top = dimStack.peek();
        if(top != null && top.equals(data)) {
            return false;
        }

        // If we have it in the list, push it up to the top...
        if(dimStack.contains(data)) {
            dimStack.remove(data);
            dimStack.push(data);
        }

        dimStack.push(data);
        return true;
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
