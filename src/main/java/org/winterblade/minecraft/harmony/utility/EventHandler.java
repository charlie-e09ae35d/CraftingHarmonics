package org.winterblade.minecraft.harmony.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
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
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.entities.EntityRegistry;
import org.winterblade.minecraft.harmony.entities.callbacks.StopTimeCommand;
import org.winterblade.minecraft.harmony.items.ItemRegistry;
import org.winterblade.minecraft.harmony.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.tileentities.TileEntityTickRegistry;
import org.winterblade.minecraft.harmony.world.ProxiedWorldProvider;
import org.winterblade.minecraft.harmony.world.WeatherRegistry;
import org.winterblade.minecraft.harmony.world.sky.ClientSkyModifications;
import org.winterblade.minecraft.harmony.world.sky.SkyModificationRegistry;

/**
 * Created by Matt on 4/13/2016.
 */
public class EventHandler {
    private boolean debounceItemRightClick;
    private boolean debounceEntityInteract;

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

        NashornConfigProcessor.getInstance().reportErrorsTo(player);

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
            WeatherRegistry.updateWeather(evt.world);

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
            ProxiedWorldProvider.injectProvider(evt.getWorld());
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
        evt.setCanceled(true);

        // The game ends up firing the RightClickItem event as well; we set this here to make sure
        // we don't end up doing all the checks/callbacks a second time in that event:
        debounceItemRightClick = true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteractEvent(PlayerInteractEvent.RightClickItem evt) {
        // If we just cancelled it for the RightClickBlock evt, don't bother:
        if(debounceItemRightClick) {
            evt.setCanceled(true);
            debounceItemRightClick = false;
            return;
        }

        // Called when right clicking with an item, always, even after right clicking a block
        if(evt.isCanceled() || !ItemRegistry.instance.shouldCancelUse(evt)) return;
        evt.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        if(evt.isCanceled() || !BlockRegistry.instance.shouldCancelPlace(evt)) return;
        evt.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract evt) {
        // If we're cancelling on this thread...
        if(debounceEntityInteract) {
            debounceEntityInteract = false;
            evt.setCanceled(true);
            return;
        }

        // We don't care about offhands if we're not cancelling them...
        if(evt.getHand() == EnumHand.OFF_HAND) return;

        // If we pass the check, then we can allow the event to go through...
        if(EntityRegistry.allowInteractionBetween(evt.getTarget(), evt.getEntityPlayer())) return;

        // Otherwise, cancel, and debounce our off-hand click:
        evt.setCanceled(true);
        debounceEntityInteract = true;
    }
}
