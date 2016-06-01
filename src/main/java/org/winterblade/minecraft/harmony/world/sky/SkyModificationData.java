package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Created by Matt on 5/31/2016.
 */
public class SkyModificationData {
    public static final SkyModificationData Default = new SkyModificationData();

    // General data
    private final int dim;

    // Sky colors:
    private boolean hasSkyColorMod = false;
    private final PriorityQueue<SkyColorMapData> skyColor = new PriorityQueue<>();
    private Vec3d lastProviderColor;
    private int skyUpdateTicksRemaining;

    private SkyModificationData() {dim = Integer.MIN_VALUE;}

    public SkyModificationData(int dim) {
        this.dim = dim;
    }

    /**
     * Updates or replaces the sky color.
     *
     * @param y
     * @param providerColor    The value coming in from the provider
     * @return                 The updated value, if any, otherwise the provider color.
     */
    public Vec3d updateSkyColor(int y, Vec3d providerColor) {
        lastProviderColor = providerColor;

        // Quick exit
        if(!hasSkyColorMod) return providerColor;

        Vec3d data = getDataAtPoint(y);
        return data != null ? data : providerColor;
    }

    public void transitionSkyColorTo(SkyColorMapData[] newColors, int ticks) {
        hasSkyColorMod = true;

        // This part is really simple...
        if(ticks <= 0 || lastProviderColor == null || skyColor.size() <= 0) {
            skyColor.clear();
            Collections.addAll(skyColor, newColors);
            return;
        }

        // This part... not so much...
        Set<SkyColorMapData> newData = new HashSet<>();

        for(SkyColorMapData data : newColors) {
            newData.add(data.transitionFrom(getDataAtPoint(data.getMinY()), ticks));
        }

        skyColor.clear();
        skyColor.addAll(newData);

        skyUpdateTicksRemaining = ticks;
    }

    /**
     * Called on world ticks client side to update any values that need updating.
     */
    public void update() {
        // Transition sky color if we have to...
        if(0 < skyUpdateTicksRemaining) {
            skyUpdateTicksRemaining--;
            skyColor.forEach(SkyColorMapData::update);
        }
    }

    /**
     * Determines the color at the given Y level
     * @param y    The Y level
     * @return     The color at the point.
     */
    private Vec3d getDataAtPoint(int y) {
        SkyColorMapData min = null, max = null;

        // Find our min and max:
        Iterator<SkyColorMapData> iterator = skyColor.iterator();
        for (; iterator.hasNext(); ) {
            SkyColorMapData data = iterator.next();
            if (y < data.getMinY()) break;
            min = data;
        }

        if(iterator.hasNext()) max = iterator.next();

        // Well this went wrong...
        if(min == null) return null;

        return max != null ? max.blendWith(min, y - min.getMinY()) : min.getAsVector();
    }

}
