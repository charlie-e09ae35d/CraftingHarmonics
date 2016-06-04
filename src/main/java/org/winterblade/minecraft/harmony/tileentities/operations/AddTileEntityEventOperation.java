package org.winterblade.minecraft.harmony.tileentities.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.tileentities.TileEntityTickRegistry;

import java.util.UUID;

/**
 * Created by Matt on 5/30/2016.
 */
@Operation(name = "addTileEntityEvent")
public class AddTileEntityEventOperation extends BasicOperation {
    /*
    * Serialized properties
    */
    private String[] what;
    private ITileEntityCallback[] events;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = TileEntityTickRegistry.registerTileEntityEvents(what, events);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding tile entity events for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        TileEntityTickRegistry.applyTileEntityEvents(ticket);
    }

    @Override
    public void undo() {
        TileEntityTickRegistry.removeTileEntityEvents(ticket);
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
