package org.winterblade.minecraft.harmony.messaging.server;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.utility.SynchronizedRandom;

/**
 * Created by Matt on 4/13/2016.
 */
public class RandomSynchronizerMessage implements IMessage {
    private long seed;

    public RandomSynchronizerMessage() {}

    public RandomSynchronizerMessage(long seed) {
        this.seed = seed;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf   The buffer to read
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        seed = buf.readLong();
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf   The buffer to write
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(seed);
    }

    public static class RandomSynchronizerMessageHandler implements IMessageHandler<RandomSynchronizerMessage, IMessage> {

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message   The message
         * @param ctx       The message context
         * @return          Nothing
         */
        @Override
        public IMessage onMessage(RandomSynchronizerMessage message, MessageContext ctx) {
            LogHelper.info("Received random seed from server: " + message.seed);
            SynchronizedRandom.setMyRandom(message.seed);
            return null;
        }
    }
}
