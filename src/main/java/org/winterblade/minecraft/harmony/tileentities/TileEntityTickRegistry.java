package org.winterblade.minecraft.harmony.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.common.TickHandler;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Matt on 5/29/2016.
 */
public class TileEntityTickRegistry {
    private TileEntityTickRegistry() {}

    private static boolean inited = false;

    private static TileEntityTickHandler<ITileEntityCallback, BaseTileEntityCallback.Handler> eventHandler;

    // Queued callbacks; this is a concurrent queue because we may add to it while processing it.
    private static Queue<TileEntityCallbackData> callbackQueue = new ConcurrentLinkedQueue<>();

    public static void init() {
        inited = true;

        eventHandler = new TileEntityTickHandler<>(BaseTileEntityCallback.Handler.class, CraftingHarmonicsMod.getConfigManager().getEventTicks());
    }

    public static UUID registerTileEntityEvents(String[] what, ITileEntityCallback[] events) {
        return eventHandler.registerHandler(what, events);
    }

    public static void applyTileEntityEvents(UUID ticket) {
        eventHandler.apply(ticket);
    }

    public static void removeTileEntityEvents(UUID ticket) {
        eventHandler.remove(ticket);
    }

    /**
     * Add callbacks to be run later.
     * @param target       The target to run them on
     * @param callbacks    The callbacks to run
     */
    public static void addCallbackSet(TileEntity target, ITileEntityCallback[] callbacks) {
        callbackQueue.add(new TileEntityCallbackData(target, callbacks));
    }

    /**
     * Process the current callback queue
     */
    public static void processCallbackQueue() {
        for (Iterator<TileEntityCallbackData> iterator = callbackQueue.iterator(); iterator.hasNext(); ) {
            TileEntityCallbackData callbackData = iterator.next();
            iterator.remove();

            try {
                callbackData.runCallbacks();
            } catch (Exception ex) {
                LogHelper.error("Error processing TileEntity callbacks.", ex);
            }

            // TODO: Make this have a configurable limiter on the number of callbacks
        }
    }

    private static class TileEntityTickHandler<TMatcher, THandler extends BaseEventMatch.BaseMatchHandler<TMatcher, TileEntity>>
            extends TickHandler<TMatcher, TileEntity, THandler> {

        TileEntityTickHandler(Class<THandler> handlerClass, int freq) {
            super(freq, handlerClass);
        }
    }

    public static void handleTick(TickEvent.WorldTickEvent evt) {
        // Init, if we have to...
        if(!inited) init();
        if(!eventHandler.isActiveThisTick(evt)) return;

        Random rand = evt.world.rand;

        List<TileEntity> entities = evt.world.tickableTileEntities;

        for(TileEntity entity : entities) {
            // Make sure we have a valid TE
            if (entity.isInvalid() || !entity.hasWorldObj()) continue;
            BlockPos blockpos = entity.getPos();

            // Make sure it's loaded and within our border:
            if (!evt.world.isBlockLoaded(blockpos) || !evt.world.getWorldBorder().contains(blockpos)) continue;

            // Now actually handle it...
            String entityName = entity.getBlockType().getLocalizedName();
            String entityClassName = entity.getClass().getName();
            eventHandler.handle(rand, entity, entityName, entityClassName);
        }
    }

    private static class TileEntityCallbackData {
        private final WeakReference<TileEntity> targetRef;
        private final ITileEntityCallback[] callbacks;

        TileEntityCallbackData(TileEntity target, ITileEntityCallback[] callbacks) {
            targetRef = new WeakReference<>(target);
            this.callbacks = callbacks;
        }

        public void runCallbacks() {
            TileEntity target = targetRef.get();
            if(target == null) return;

            for(ITileEntityCallback callback : callbacks) {
                callback.apply(target);
            }
        }
    }
}
