package org.winterblade.minecraft.harmony.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.blocks.BlockDropRegistry;
import org.winterblade.minecraft.harmony.crafting.messaging.PacketHandler;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.MobShedRegistry;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

/**
 * Created by Matt on 4/13/2016.
 */
public class EventHandler {
    @SubscribeEvent
    // This is called on the server.
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent evt) {
        EntityPlayer player = evt.player;

        if(!(player instanceof EntityPlayerMP)) return;

        long seed = player.getEntityWorld().getTotalWorldTime() + player.getEntityWorld().getSeed();
        LogHelper.info("Player logged in, getting them a new random seed for crafting: " + seed);

        SynchronizedRandom.generateNewRandom(player.getUniqueID().toString(), seed);
        PacketHandler.synchronizeRandomToPlayer(seed, (EntityPlayerMP)player);

        // If we're doing SP, we need to set up the base sets...
        if(FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
            CraftingHarmonicsMod.applyBaseSets();
        }

        PacketHandler.synchronizeConfig(NashornConfigProcessor.getInstance().getCache(), (EntityPlayerMP)player);
    }


    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent evt) {
        if(evt.phase != TickEvent.Phase.END) return;

        CraftingHarmonicsMod.checkDifficultyChanged();
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent evt) {
        if(evt.phase != TickEvent.Phase.END || evt.world.isRemote) return;

        try {
            MobShedRegistry.handleSheds(evt);

            // We should (hopefully) be done dealing with the explosion by this point...
            BlockDropRegistry.clearExplodedList();
        } catch(Exception ex) {
            LogHelper.error("Error handling world tick; please report this along with your config file.", ex);
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
}
