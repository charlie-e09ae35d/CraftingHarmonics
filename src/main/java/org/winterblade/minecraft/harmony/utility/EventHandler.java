package org.winterblade.minecraft.harmony.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.crafting.messaging.PacketHandler;
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
        System.out.println("Player logged in, getting them a new random seed for crafting: " + seed);

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
}
