package org.winterblade.minecraft.harmony.crafting.messaging;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.crafting.messaging.server.RandomSynchronizerMessage;
import org.winterblade.minecraft.harmony.crafting.messaging.server.RandomSynchronizerMessage.RandomSynchronizerMessageHandler;

/**
 * Created by Matt on 4/13/2016.
 */
public class PacketHandler {
    public static final SimpleNetworkWrapper wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(CraftingHarmonicsMod.MODID);

    static {
        int id = 0;

        // Register our messages:
        wrapper.registerMessage(RandomSynchronizerMessageHandler.class, RandomSynchronizerMessage.class,
                id++, Side.CLIENT);
    }

    /**
     * Send the random synchronizer to the player
     * @param seed      The seed to synchronize
     * @param player    The player to send it to.
     */
    public static void synchronizeRandomToPlayer(long seed, EntityPlayerMP player) {
        wrapper.sendTo(new RandomSynchronizerMessage(seed), player);
    }
}
