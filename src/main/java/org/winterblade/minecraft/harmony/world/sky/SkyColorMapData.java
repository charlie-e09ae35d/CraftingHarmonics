package org.winterblade.minecraft.harmony.world.sky;

/**
 * Created by Matt on 5/31/2016.
 */
public class SkyColorMapData implements Comparable<SkyColorMapData> {
    private int minY;
    private double r;
    private double g;
    private double b;
    private String mode = "immediate";

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
}
