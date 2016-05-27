package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "stopTime")
public class StopTimeCommand extends BaseEntityAndDimensionCallback {
    private static final HashMap<Integer, TimeStopData> timeStopsInProgress = new HashMap<>();

    /*
     * Serialized properties
     */
    private int duration;
    private IEntityCallbackContainer[] onFailure;
    private IEntityCallbackContainer[] onTimeStopStart;
    private IEntityCallbackContainer[] onTimeStopExtended;
    private IEntityCallbackContainer[] onTimeStopEnd;
    private IEntityCallbackContainer[] onComplete;

    /*
     * Computed properties
     */
    private transient UUID id;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        id = UUID.randomUUID();
    }

    @Override
    protected void applyWithTargetDimension(Entity target, int targetDim) {
        WorldServer worldServer = DimensionManager.getWorld(targetDim);
        if(worldServer == null) {
            LogHelper.error("Attempted to stop the time for a world (" + targetDim + ") that doesn't exist.");
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        // Make sure we're not already doing this...
        if(timeStopsInProgress.containsKey(targetDim)) {
            timeStopsInProgress.get(targetDim).extend(this, target);
            runCallbacks(onTimeStopExtended, target);
            runCallbacks(onComplete, target);
            return;
        }

        // If an op has disabled time in the world anyway
        if(!worldServer.getGameRules().getBoolean("doDaylightCycle")) {
            LogHelper.warn("World (" + targetDim + ") already has time stopped by something else.");
            runCallbacks(onFailure, target);
            runCallbacks(onComplete, target);
            return;
        }

        // Otherwise, this is a new time stop...
        timeStopsInProgress.put(targetDim, new TimeStopData(targetDim, worldServer.getTotalWorldTime()+duration, this, target));
        runCallbacks(onTimeStopStart, target);
        runCallbacks(onComplete, target);
        worldServer.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");

        // TODO: Also prevent sleeping.
    }

    /**
     * Check to see if the given world has a time stop going on...
     * @param world    The world to check
     */
    public static void checkTimeStops(World world) {
        int dimId = world.provider.getDimension();
        if(!timeStopsInProgress.containsKey(dimId)) return;

        TimeStopData data = timeStopsInProgress.get(dimId);

        // If we haven't reached the proper time, ignore it for now...
        if(!data.isFinished(world.getTotalWorldTime())) return;

        // Otherwise, finish it.
        data.done(DimensionManager.getWorld(dimId));
        timeStopsInProgress.remove(dimId);
    }

    public static class TimeStopData {
        private final int targetDim;
        private long timeStopEnd;
        private HashMap<UUID, TimeStopCause> includedCommands = new HashMap<>();

        public TimeStopData(int targetDim, long timeStopEnd, StopTimeCommand command, Entity entity) {
            this.targetDim = targetDim;
            this.timeStopEnd = timeStopEnd;
            includedCommands.put(command.id, new TimeStopCause(command, entity));

            // TODO: Add to saved game
        }

        /**
         * Adds the given duration to the existing value
         * @param command    The command extending the duration.
         * @param entity     The entity causing the time stop
         */
        private void extend(StopTimeCommand command, Entity entity) {
            timeStopEnd += command.duration;

            if(includedCommands.containsKey(command.id)) {
                includedCommands.get(command.id).addCause(entity);
            } else {
                includedCommands.put(command.id, new TimeStopCause(command, entity));
            }

            // TODO: Update saved game
        }

        /**
         * Check to see if this time stop is finished
         * @param currentTime    The current time of the given world
         * @return               True if the time stop is over; false otherwise.
         */
        public boolean isFinished(long currentTime) {
            return timeStopEnd <= currentTime;
        }

        public void done(WorldServer worldServer) {
            worldServer.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");

            for(TimeStopCause cause : includedCommands.values()) {
                cause.done(worldServer);
            }

            // TODO: Update saved game
        }
    }

    public static class TimeStopCause {
        private final StopTimeCommand command;
        private final Set<UUID> entityIds = new HashSet<>();

        public TimeStopCause(StopTimeCommand command, Entity entity) {
            this.command = command;
            this.entityIds.add(entity.getPersistentID());
        }

        public void addCause(Entity entity) {
            this.entityIds.add(entity.getPersistentID());
        }

        public void done(WorldServer worldServer) {
            for(UUID entityId : entityIds) {
                command.done(worldServer.getEntityFromUuid(entityId), entityId);
            }
        }
    }

    /**
     * Called when the time stop ends
     * @param target    The target the time stop was caused by.  This will not be called if the target is not
     *                  in the time stopped world when the time stop ends.
     * @param entityId  The entity ID to search for
     */
    private void done(@Nullable Entity target, UUID entityId) {
        // If we have a target in the world, run the command...
        if(target != null) {
            runCallbacks(onTimeStopEnd, target);
            return;
        }

        // If we don't, check to see if it's a player; track them to the ends of the universe...
        try {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(entityId);
            //noinspection ConstantConditions <-- not always null, IDEA
            if(player == null) return;

            runCallbacks(onTimeStopEnd, player);
        }
        catch(Exception ex) {
            // Probably expected at this point.
        }
    }
}
