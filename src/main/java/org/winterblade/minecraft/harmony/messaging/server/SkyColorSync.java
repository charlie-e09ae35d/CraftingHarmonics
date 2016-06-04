package org.winterblade.minecraft.harmony.messaging.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.winterblade.minecraft.harmony.world.sky.ClientSkyModifications;
import org.winterblade.minecraft.harmony.world.sky.SkyColorMapData;

/**
 * Created by Matt on 6/1/2016.
 */
public class SkyColorSync implements IMessage {
    private int dim;
    private int transitionTime;
    private SkyColorMapData[] colormap;

    public SkyColorSync() {}

    public SkyColorSync(int dim, int transitionTime, SkyColorMapData[] colormap) {
        this.dim = dim;
        this.transitionTime = transitionTime;
        this.colormap = colormap;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        transitionTime = buf.readInt();

        colormap = new SkyColorMapData[buf.readByte()];

        for(int i = 0; i < colormap.length; i++) {
            colormap[i] = SkyColorMapData.fromBytes(buf);
        }
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(transitionTime);
        buf.writeByte(colormap.length);

        for(SkyColorMapData data : colormap) {
            SkyColorMapData.toBytes(data, buf);
        }
    }

    public static class Handler implements IMessageHandler<SkyColorSync, IMessage> {
        @Override
        public IMessage onMessage(SkyColorSync message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ClientSkyModifications.transitionSkyColor(message.dim, message.transitionTime, message.colormap);
            });
            return null;
        }
    }
}
