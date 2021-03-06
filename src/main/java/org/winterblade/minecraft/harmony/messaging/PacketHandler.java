package org.winterblade.minecraft.harmony.messaging;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.messaging.server.ConfigSyncMessage;
import org.winterblade.minecraft.harmony.messaging.server.RandomSynchronizerMessage;
import org.winterblade.minecraft.harmony.messaging.server.RandomSynchronizerMessage.RandomSynchronizerMessageHandler;
import org.winterblade.minecraft.harmony.messaging.server.SkyColorSync;

import java.util.Map;

/**
 * Created by Matt on 4/13/2016.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(CraftingHarmonicsMod.MODID);

    public static void registerMessages() {
        int id = 0;

        // Register our messages:
        wrapper.registerMessage(RandomSynchronizerMessageHandler.class, RandomSynchronizerMessage.class,
                id++, Side.CLIENT);
        wrapper.registerMessage(ConfigSyncMessage.Handler.class, ConfigSyncMessage.class, id++, Side.CLIENT);
        wrapper.registerMessage(SkyColorSync.Handler.class, SkyColorSync.class, id++, Side.CLIENT);
    }

    /**
     * Send the random synchronizer to the player
     * @param seed      The seed to synchronize
     * @param player    The player to send it to.
     */
    public static void synchronizeRandomToPlayer(long seed, EntityPlayerMP player) {
        wrapper.sendTo(new RandomSynchronizerMessage(seed), player);
    }

    /**
     * Sends the config cache to the player
     * @param
     */
    public static void synchronizeConfig(Map<String,String> config, EntityPlayerMP player) {
        wrapper.sendTo(new ConfigSyncMessage(config, CraftingHarmonicsMod.getAppliedSets()), player);
    }
}
