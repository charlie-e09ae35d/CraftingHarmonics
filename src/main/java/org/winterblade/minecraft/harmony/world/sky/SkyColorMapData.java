package org.winterblade.minecraft.harmony.world.sky;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Matt on 5/31/2016.
 */
public class SkyColorMapData implements Comparable<SkyColorMapData> {
    private int minY;
    private double r;
    private double g;
    private double b;
    private boolean quick;
    private double rPerTick, gPerTick, bPerTick;

    public int getMinY() {
        return minY;
    }

    public double getR() {
        return r;
    }

    public double getG() {
        return g;
    }

    public double getB() {
        return b;
    }

    @Override
    public int compareTo(SkyColorMapData o) {
        return this.minY - o.minY;
    }

    public Vec3d getAsVector() {
        return new Vec3d(r, g, b);
    }

    public Vec3d blendWith(SkyColorMapData other, int offset) {
        // If we're doing a quick render:
        if(quick) return new Vec3d(r,g,b);

        // Otherwise, do all this mess...
        int d = Math.abs(minY-other.getMinY());
        double r = (((getR()-other.getR())/d)*offset)+other.getR();
        double g = (((getG()-other.getG())/d)*offset)+other.getG();
        double b = (((getB()-other.getB())/d)*offset)+other.getB();
        return new Vec3d(r,g,b);
    }

    public void update() {
        r += rPerTick;
        g += gPerTick;
        b += bPerTick;
    }

    // For transitions
    public SkyColorMapData transitionFrom(Vec3d dataAtPoint, int ticks) {
        SkyColorMapData output = new SkyColorMapData();
        output.minY = minY;

        // Figure out our transitions...
        if(dataAtPoint != null) {
            output.rPerTick = (r - dataAtPoint.xCoord) / ticks;
            output.gPerTick = (g - dataAtPoint.yCoord) / ticks;
            output.bPerTick = (b - dataAtPoint.zCoord) / ticks;

            // Deal with our existing things...
            output.r = dataAtPoint.xCoord;
            output.g = dataAtPoint.yCoord;
            output.b = dataAtPoint.zCoord;
        } else {
            output.r = r;
            output.g = g;
            output.b = b;
        }

        return output;
    }

    /**
     * Deserialize a {@link SkyColorMapData} from the {@link ByteBuf}
     * @param buf    The buffer to read from
     * @return       The parsed map data
     */
    public static SkyColorMapData fromBytes(ByteBuf buf) {
        SkyColorMapData output = new SkyColorMapData();
        output.minY = buf.readInt();
        output.r = buf.readDouble();
        output.g = buf.readDouble();
        output.b = buf.readDouble();
        output.quick = buf.readBoolean();

        return output;
    }

    /**
     * Serialize the {@link SkyColorMapData} into a {@link ByteBuf}
     * @param data The data to serialize
     * @param buf  The buffer to serialize to
     */
    public static void toBytes(SkyColorMapData data, ByteBuf buf) {
        buf.writeInt(data.getMinY());
        buf.writeDouble(data.getR());
        buf.writeDouble(data.getG());
        buf.writeDouble(data.getB());
        buf.writeBoolean(data.quick);
    }
}
