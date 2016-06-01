package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.util.math.Vec3d;

/**
 * Created by Matt on 5/31/2016.
 */
public class SkyColorMapData implements Comparable<SkyColorMapData> {
    private int minY;
    private double r;
    private double g;
    private double b;
    private boolean blend;
    private String mode = "immediate";
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

    public String getMode() {
        return mode;
    }

    @Override
    public int compareTo(SkyColorMapData o) {
        return this.minY - o.minY;
    }

    public Vec3d getAsVector() {
        return new Vec3d(r, g, b);
    }

    public Vec3d blendWith(SkyColorMapData other, int offset) {
        int d = Math.abs(minY-other.getMinY());
        double r = (Math.abs(getR()-other.getR())/d)*offset;
        double g = (Math.abs(getG()-other.getG())/d)*offset;
        double b = (Math.abs(getB()-other.getB())/d)*offset;
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
        output.rPerTick = (r - dataAtPoint.xCoord)/ticks;
        output.gPerTick = (g - dataAtPoint.yCoord)/ticks;
        output.bPerTick = (b - dataAtPoint.zCoord)/ticks;

        // Deal with our existing things...
        output.r = dataAtPoint.xCoord;
        output.g = dataAtPoint.yCoord;
        output.b = dataAtPoint.zCoord;

        return output;
    }
}
