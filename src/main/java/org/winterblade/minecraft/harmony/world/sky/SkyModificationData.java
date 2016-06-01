package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.util.math.Vec3d;

/**
 * Created by Matt on 5/31/2016.
 */
public class SkyModificationData {
    public static final SkyModificationData Default = new SkyModificationData();

    // Sky colors:
    private Vec3d skyColor;
    private Vec3d lastProviderColor;
    private double skyRPerTick, skyGPerTick, skyBPerTick;
    private int skyUpdateTicksRemaining;

    /**
     * Updates or replaces the sky color.
     * @param providerColor    The value coming in from the provider
     * @return                 The updated value, if any, otherwise the provider color.
     */
    public Vec3d updateSkyColor(Vec3d providerColor) {
        lastProviderColor = providerColor;
        return skyColor != null ? skyColor : providerColor;
    }

    public void transitionSkyColorTo(SkyColorMapData[] newColor, int ticks) {
        // If we have no target, go ahead and use the last provider color:
        if(skyColor == null) skyColor = lastProviderColor;

        // If we're not transitioning gently:
        if(ticks <= 0 || skyColor == null) {
            skyColor = new Vec3d(newColor[0].getR(), newColor[0].getG(), newColor[0].getB());
            return;
        }

        // Linear transitions FTW!
        skyRPerTick = (newColor[0].getR() - skyColor.xCoord)/ticks;
        skyGPerTick = (newColor[0].getG() - skyColor.yCoord)/ticks;
        skyBPerTick = (newColor[0].getB() - skyColor.zCoord)/ticks;
        skyUpdateTicksRemaining = ticks;
    }

    /**
     * Called on world ticks client side to update any values that need updating.
     */
    public void update() {
        // Transition sky color if we have to...
        if(0 < skyUpdateTicksRemaining) {
            skyUpdateTicksRemaining--;
            skyColor = skyColor.addVector(skyRPerTick, skyGPerTick, skyBPerTick);
        }
    }
}
