package org.winterblade.minecraft.harmony.crafting.messaging.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.crafting.integration.jei.Jei;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Matt on 4/15/2016.
 */
public class ConfigSyncMessage implements IMessage {
    private Collection<String> config;
    private Set<String> appliedSets;

    public ConfigSyncMessage() {}

    public ConfigSyncMessage(Map<String, String> config, Set<String> appliedSets) {
        this.config = config.values();
        this.appliedSets = appliedSets;
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
            int len = buf.readInt();
            byte[] configBuf = new byte[len];
            buf.readBytes(configBuf);
            config.add(new String(configBuf));
        }

        appliedSets = new HashSet<>();

        // Read in what sets we have applied:
        int setSize = buf.readInt();
        for(int i = 0; i < setSize; i++) {
            appliedSets.add(ByteBufUtils.readUTF8String(buf));
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
            try {
                buf.writeInt(entry.length());
                buf.writeBytes(entry.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO: Handle this better.
                e.printStackTrace();
            }
        }

        // Write out the applied sets
        buf.writeInt(appliedSets.size());

        for(String set : appliedSets) {
            ByteBufUtils.writeUTF8String(buf, set);
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
            LogHelper.info("Received configuration from the server.");

            CraftingHarmonicsMod.clearSets();

            boolean badConfig = false;
            for(String file : message.config) {
                try {
                    NashornConfigProcessor.getInstance().processConfig(file);
                } catch (Exception e) {
                    badConfig = true;
                    LogHelper.error("Error reading config the server sent; some recipes will be wrong." + e);
                }
            }

            if(badConfig) {
                Minecraft.getMinecraft().thePlayer
                        .addChatMessage(new TextComponentString("Crafting Harmonics: There was an issue processing " +
                                "some of the configuration the server sent over.  Some of your recipes may not work."));
            }

            CraftingHarmonicsMod.initSets();
            CraftingHarmonicsMod.applySets(message.appliedSets.toArray(new String[message.appliedSets.size()]));
            Jei.reloadJEI();

            return null;
        }
    }
}
