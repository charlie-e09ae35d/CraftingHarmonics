package org.winterblade.minecraft.harmony.utility;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.OperationSet;
import org.winterblade.minecraft.harmony.blocks.BlockDropRegistry;
import org.winterblade.minecraft.harmony.blocks.BlockRegistry;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.callbacks.StopTimeCommand;
import org.winterblade.minecraft.harmony.items.ItemRegistry;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.tileentities.TileEntityTickRegistry;
import org.winterblade.minecraft.harmony.world.ProxiedWorldProvider;
import org.winterblade.minecraft.harmony.world.sky.ClientSkyModifications;
import org.winterblade.minecraft.harmony.world.sky.SkyModificationRegistry;

/**
 * Created by Matt on 4/13/2016.
 */
public class EventHandler {
    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        EntityPlayer basePlayer = evt.player;

        if(!(basePlayer instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP)basePlayer;

        long seed = player.getEntityWorld().getTotalWorldTime() + player.getEntityWorld().getSeed();
        LogHelper.info("Player logged in, getting them a new random seed for crafting: " + seed);

        SynchronizedRandom.generateNewRandom(player.getUniqueID().toString(), seed);
        PacketHandler.synchronizeRandomToPlayer(seed, player);

        // If we're doing SP, we need to set up the base sets...
        if(FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
            CraftingHarmonicsMod.applyBaseSets();
        } else {
            // Apply our per-player operations...
            OperationSet.runPerPlayerOperations(player);
        }

        PacketHandler.synchronizeConfig(NashornConfigProcessor.getInstance().getCache(), player);
        SkyModificationRegistry.resyncPlayerData(player);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent evt) {
        if(evt.phase != TickEvent.Phase.END) return;

        try {
            CraftingHarmonicsMod.checkServerTick();
            MobTickRegistry.processCallbackQueue();
            TileEntityTickRegistry.processCallbackQueue();
        } catch (Exception ex) {
            LogHelper.error("Error handling server tick; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        if(evt.phase != TickEvent.Phase.END || evt.world.isRemote) return;

        try {
            MobTickRegistry.handleTick(evt);
            TileEntityTickRegistry.handleTick(evt);
            StopTimeCommand.checkTimeStops(evt.world);

            // We should (hopefully) be done dealing with the explosion by this point...
            BlockDropRegistry.clearExplodedList();
        } catch(Exception ex) {
            LogHelper.error("Error handling world tick; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
       if(evt.phase != TickEvent.Phase.END) return;

        try {
            ClientSkyModifications.update();
        } catch (Exception ex) {
            LogHelper.error("Error handling client tick; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent
    public void onMobDrop(LivingDropsEvent evt) {
        if(evt.isCanceled()) return;

        try {
            MobDropRegistry.handleDrops(evt);
        } catch(Exception ex) {
            LogHelper.error("Error handling drop event; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockDrop(BlockEvent.HarvestDropsEvent evt) {
        if(evt.isCanceled()) return;

        try {
            BlockDropRegistry.handleDrops(evt);
        } catch (Exception ex) {
            LogHelper.error("Error handling block drop event; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onExplosion(ExplosionEvent.Detonate evt) {
        if(evt.isCanceled()) return;

        try {
            BlockDropRegistry.registerExplodedBlocks(evt.getAffectedBlocks());
        } catch (Exception ex) {
            LogHelper.error("Error handling explosion detonation event; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldLoaded(WorldEvent.Load evt) {
        try {
            if(evt.getWorld().isRemote) {
                ProxiedWorldProvider.injectProvider(evt.getWorld());
            }
        } catch (Exception | VerifyError ex) {
            LogHelper.error("Error handling world load event; please report this along with your config file.", ex);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent.RightClickBlock evt) {
        // Called when right clicking on a block (potentially with something...)
        if(evt.isCanceled() || !ItemRegistry.instance.shouldCancelUse(evt)) return;

        // If we're cancelling, should set:
        evt.setUseItem(Event.Result.DENY);
        evt.setUseBlock(Event.Result.ALLOW);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent.RightClickItem evt) {
        // Called when right clicking with an item, on nothing
        if(evt.isCanceled() || !ItemRegistry.instance.shouldCancelUse(evt)) return;
        evt.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        if(evt.isCanceled() || !BlockRegistry.instance.shouldCancelPlace(evt)) return;
        evt.setCanceled(true);
    }
}
