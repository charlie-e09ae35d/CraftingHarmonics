package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matt on 6/1/2016.
 */
public class SkyModificationRegistry {
    private SkyModificationRegistry() {}

    private static final Map<Integer, Data> baseColors = new HashMap<>();
    private static final Map<Integer, Data> tempColors = new HashMap<>();
    private static final Map<UUID, Map<Integer, Data>> perPlayerColors = new HashMap<>();

    /**
     * Set the base for a given dimension; reverting will revert to this, unless given the proper option.
     * @param data    The data to transition to.
     */
    public static void setDimensionBase(Data data) {
        // If we've already applied it...
        if(baseColors.containsKey(data.getTargetDim()) && baseColors.get(data.getTargetDim()).getHash().equals(data.getHash())) {
            return;
        }

        baseColors.put(data.getTargetDim(), data);
        PacketHandler.wrapper.sendToAll(new SkyColorSync(data.getTargetDim(), data.getTransitionTime(), data.getColormap()));
    }

    /**
     * Run a temporary modification of the sky colors for a dimension; reverting will go back to either the base from
     * an operation, or the provider default.
     * @param data    The data to transition to.
     */
    public static void runModification(Data data) {
        // If we've already applied it...
        if(tempColors.containsKey(data.getTargetDim()) && tempColors.get(data.getTargetDim()).getHash().equals(data.getHash())) {
            return;
        }

        tempColors.put(data.getTargetDim(), data);
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

        Map<Integer, Data> playerData = perPlayerColors.get(target.getPersistentID());
        if (playerData == null) {
            // Do we need to stash a new entry?
            playerData = new HashMap<>();
            perPlayerColors.put(target.getPersistentID(), playerData);
        } else if(playerData.containsKey(data.getTargetDim()) && playerData.get(data.getTargetDim()).getHash().equals(data.getHash())) {
            // Or, if we match our current entry, just leave now...
            return true;
        }

        // Save our data and send it out.
        playerData.put(data.getTargetDim(), data);
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
    }
}
