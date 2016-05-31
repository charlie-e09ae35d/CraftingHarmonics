package org.winterblade.minecraft.harmony.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.BaseEventMatch;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.common.TickHandler;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Matt on 5/29/2016.
 */
public class TileEntityTickRegistry {
    private TileEntityTickRegistry() {}

    private static boolean inited = false;

    private static TileEntityTickHandler<BaseTileEntityCallback, BaseTileEntityCallback.Handler> eventHandler;

    public static void init() {
        inited = true;

        eventHandler = new TileEntityTickHandler<>(BaseTileEntityCallback.Handler.class, CraftingHarmonicsMod.getConfigManager().getEventTicks());
    }

    public static UUID registerTileEntityEvents(String[] what, BaseTileEntityCallback[] events) {
        return eventHandler.registerHandler(what, events);
    }

    public static void applyTileEntityEvents(UUID ticket) {
        eventHandler.apply(ticket);
    }

    public static void removeTileEntityEvents(UUID ticket) {
        eventHandler.remove(ticket);
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
}
