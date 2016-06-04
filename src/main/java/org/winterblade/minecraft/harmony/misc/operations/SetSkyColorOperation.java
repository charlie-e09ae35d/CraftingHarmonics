package org.winterblade.minecraft.harmony.misc.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.world.sky.SkyColorMapData;
import org.winterblade.minecraft.harmony.world.sky.SkyModificationRegistry;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Matt on 6/2/2016.
 */
@Operation(name = "setSkyColor")
public class SetSkyColorOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    int dimension;
    int transitionTime;
    SkyColorMapData[] colormap;

    /*
     * Computed properties
     */
    private transient String hash;
    private transient SkyModificationRegistry.Data data;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(colormap == null) throw new RuntimeException("setSkyColor's 'colormap' property must be provided.");

        hash = Joiner.on("-").join(Arrays.stream(colormap).map(SkyColorMapData::toString).collect(Collectors.toList()));
        data = new SkyModificationRegistry.Data(dimension, transitionTime, colormap, hash);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        SkyModificationRegistry.runModification(data);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        SkyModificationRegistry.removeModification(data);
    }

    /**
     * Should the operation be initialized/applied/undone/etc on the client
     *
     * @return True if it should, false otherwise.  Defaults to true.
     */
    @Override
    public boolean isClientOperation() {
        return false;
    }
}
