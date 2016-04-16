package org.winterblade.minecraft.harmony.crafting.messaging.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.CraftingSet;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.*;

/**
 * Created by Matt on 4/15/2016.
 */
public class ConfigSyncMessage implements IMessage {
    private Collection<String> config;

    public ConfigSyncMessage() {}

    public ConfigSyncMessage(Map<String, String> config) {
        this.config = config.values();
    }
    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf   The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        config = new ArrayList<>();

        int size = buf.readInt();

        // Read the contents of the buffer until we've gotten all the configs...
        for(int i = 0; i < size; i++) {
            config.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf   The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(config.size());

        for(String entry : config) {
            ByteBufUtils.writeUTF8String(buf, entry);
        }
    }

    public static class Handler implements IMessageHandler<ConfigSyncMessage, IMessage> {

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx
         * @return an optional return message
         */
        @Override
        public IMessage onMessage(ConfigSyncMessage message, MessageContext ctx) {
            CraftingHarmonicsMod.logger.info("Received configuration from the server.");

            CraftingHarmonicsMod.clearSets();

            boolean badConfig = false;
            for(String file : message.config) {
                try {
                    NashornConfigProcessor.getInstance().processConfig(file);
                } catch (Exception e) {
                    badConfig = true;
                    CraftingHarmonicsMod.logger.error("Error reading config the server sent; some recipes will be wrong." + e);
                }
            }

            if(badConfig) {
                Minecraft.getMinecraft().thePlayer
                        .addChatMessage(new TextComponentString("Crafting Harmonics: There was an issue processing " +
                                "some of the configuration the server sent over.  Some of your recipes may not work."));
            }

            CraftingHarmonicsMod.initSets();
            CraftingHarmonicsMod.applySets(new String[] {"default"});

            return null;
        }
    }
}
