package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "stopTime")
public class StopTimeCommand extends BaseEntityAndDimensionCallback {
    public static final String RUNNING_TIME_STOPS = "RunningTimeStops";

    private static final HashMap<String, StopTimeCommand> timeStops = new HashMap<>();
    private static final HashMap<Integer, TimeStopData> timeStopsInProgress = new HashMap<>();

    private static final String END_TICK_TAG_NAME = "EndTick";
    private static final String CAUSES_TAG_NAME = "Causes";

    /*
     * Serialized properties
     */
    private int duration;
    private IEntityCallback[] onFailure;
    private IEntityCallback[] onTimeStopStart;
    private IEntityCallback[] onTimeStopExtended;
    private IEntityCallback[] onTimeStopEnd;
    private IEntityCallback[] onComplete;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(id == null || id.equals("")) throw new RuntimeException("All stopTime events must have an 'id' property.");
        timeStops.put(id, this);
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
            CraftingHarmonicsMod.updateSavedData();
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
        CraftingHarmonicsMod.updateSavedData();
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
        CraftingHarmonicsMod.updateSavedData();
    }

    /**
     * Deserialize the time stops from the given NBT saved data tag
     * @param nbt    The NBT from the saved game.
     */
    public static void deserializeTimeStops(NBTTagCompound nbt) {
        if (!nbt.hasKey(RUNNING_TIME_STOPS)) return;

        timeStopsInProgress.clear();

        NBTTagCompound tag = nbt.getCompoundTag(RUNNING_TIME_STOPS);
        Set<String> dims = tag.getKeySet();

        for(String dimIdStr : dims) {
            try {
                int dimId = Integer.parseInt(dimIdStr);
                parseTimeStop(dimId, tag.getCompoundTag(dimIdStr));
            } catch (Exception ex) {
                LogHelper.warn("Unable to read a time stop for dimension '{}' from the world save.", dimIdStr, ex);
            }
        }
    }

    /**
     * Parses a time stop from the given NBT tag
     * @param dimId    The dimension ID to parse for
     * @param tag      The tag to parse
     */
    public static void parseTimeStop(int dimId, NBTTagCompound tag) {
        long stopTime = tag.getLong(END_TICK_TAG_NAME);

        // Yeah, this is bad...
        if(stopTime <= 0) throw new RuntimeException("Couldn't read end time for time stop.");

        TimeStopData data = new TimeStopData(dimId, stopTime);

        NBTTagCompound commands = tag.getCompoundTag(CAUSES_TAG_NAME);
        Set<String> comIds = commands.getKeySet();

        for(String comId : comIds) {
            if(!timeStops.containsKey(comId)) continue;

            TimeStopCause cause = new TimeStopCause(timeStops.get(comId));

            NBTTagList entities = commands.getTagList(comId, 8);
            int count = entities.tagCount();

            // Add all of our entities; if this throws an exception, just bail on the entire thing.
            for(int i = 0; i < count; i++) {
                cause.addCause(UUID.fromString(entities.getStringTagAt(i)));
            }

            data.addCause(comId, cause);
        }

        // Go ahead and add it...
        timeStopsInProgress.put(dimId, data);
    }

    /**
     * Serializes the running time stops to NBT
     * @return  The NBT
     */
    public static NBTTagCompound serializeTimeStops() {
        NBTTagCompound rootTag = new NBTTagCompound();

        for(Map.Entry<Integer, TimeStopData> entry : timeStopsInProgress.entrySet()) {
            NBTTagCompound tsDataTag = new NBTTagCompound();
            tsDataTag.setLong(END_TICK_TAG_NAME, entry.getValue().timeStopEnd);

            // Iterate the causes:
            NBTTagCompound causesTag = new NBTTagCompound();

            for(TimeStopCause cause : entry.getValue().includedCommands.values()) {
                NBTTagList entitiesTag = new NBTTagList();

                for(UUID id : cause.entityIds) {
                    entitiesTag.appendTag(new NBTTagString(id.toString()));
                }

                causesTag.setTag(cause.command.id, entitiesTag);
            }

            tsDataTag.setTag(CAUSES_TAG_NAME, causesTag);
            // Add the tag to our list...
            rootTag.setTag(entry.getKey().toString(), tsDataTag);
        }

        return rootTag;
    }

    public static class TimeStopData {
        private final int targetDim;
        private long timeStopEnd;
        private HashMap<String, TimeStopCause> includedCommands = new HashMap<>();

        public TimeStopData(int targetDim, long timeStopEnd) {
            this.targetDim = targetDim;
            this.timeStopEnd = timeStopEnd;
        }

        public TimeStopData(int targetDim, long timeStopEnd, StopTimeCommand command, Entity entity) {
            this(targetDim, timeStopEnd);
            includedCommands.put(command.id, new TimeStopCause(command, entity));
        }

        /**
         * Adds the given duration to the existing value
         * @param command    The command extending the duration.
         * @param entity     The entity causing the time stop
         */
        private void extend(StopTimeCommand command, Entity entity) {
            timeStopEnd += command.duration;
            addCause(command, entity);
        }

        /**
         * Add a cause without adding duration
         * @param command    The command to add
         * @param entity     The entity involved
         */
        private void addCause(StopTimeCommand command, Entity entity) {
            if(includedCommands.containsKey(command.id)) {
                includedCommands.get(command.id).addCause(entity);
            } else {
                includedCommands.put(command.id, new TimeStopCause(command, entity));
            }
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
        }

        /**
         * Directly add a cause
         * @param id       The ID to add it to
         * @param cause    The cause to add
         */
        public void addCause(String id, TimeStopCause cause) {
            includedCommands.put(id, cause);
        }
    }

    public static class TimeStopCause {
        private final StopTimeCommand command;
        private final Set<UUID> entityIds = new HashSet<>();

        public TimeStopCause(StopTimeCommand command) {
            this.command = command;
        }

        public TimeStopCause(StopTimeCommand command, Entity entity) {
            this(command);
            this.entityIds.add(entity.getPersistentID());
        }

        public void addCause(Entity entity) {
            this.entityIds.add(entity.getPersistentID());
        }

        public void addCause(UUID uuid) {
            this.entityIds.add(uuid);
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
