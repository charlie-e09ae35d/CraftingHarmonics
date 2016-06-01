package org.winterblade.minecraft.harmony.world.sky;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 5/31/2016.
 */
public class ClientSkyModifications {
    private ClientSkyModifications() {}

    private final static Map<Integer, SkyModificationData> dimensionMods = new HashMap<>();

    /**
     * Gets the sky color for the given dimension
     * @param skyColor     The sky color to get
     * @param dimension    The dimension to get it for
     * @return             The updated sky color
     */
    public static Vec3d getSkyColorFor(Entity entity, Vec3d skyColor, int dimension) {
        return getModsFor(dimension).updateSkyColor(entity.getPosition().getY(), skyColor);
    }

    /**
     * Transition the target dimension to the given colormap
     * @param dim          The dimension to transition
     * @param time         The time to transition
     * @param colormap     The color map for the dimension
     */
    public static void transitionSkyColor(int dim, int time, SkyColorMapData[] colormap) {
        getOrCreateModFor(dim).transitionSkyColorTo(colormap, time);
    }

    /**
     * Update the modification data, if we have any.
     */
    public static void update() {
        dimensionMods.values().forEach(SkyModificationData::update);
    }

    /**
     * Get the data for the given dimension, or the default data if none exists
     * @param dimension    The dimension to get
     * @return             The modification data
     */
    private static SkyModificationData getModsFor(int dimension) {
        SkyModificationData skyModificationData = dimensionMods.get(dimension);
        return skyModificationData != null ? skyModificationData : SkyModificationData.Default;
    }

    /**
     * Gets the data for the given dimension, creating it if it doesn't exist.
     * @param dimension    The dimension to get
     * @return             The modification data
     */
    private static SkyModificationData getOrCreateModFor(int dimension) {
        SkyModificationData data = dimensionMods.get(dimension);
        if(data != null) return data;

        // Create it:
        data = new SkyModificationData(dimension);
        dimensionMods.put(dimension, data);
        return data;
    }
}
