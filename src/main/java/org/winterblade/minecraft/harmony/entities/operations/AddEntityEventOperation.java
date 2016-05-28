package org.winterblade.minecraft.harmony.entities.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import java.util.UUID;

/**
 * Created by Matt on 5/27/2016.
 */
@Operation(name = "addEntityEvent")
public class AddEntityEventOperation extends BasicOperation {
    /*
    * Serialized properties
    */
    private String[] what;
    private IEntityCallbackContainer[] events;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = MobTickRegistry.registerEntityEffects(what, events);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding entity events for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobTickRegistry.applyEntityEffects(ticket);
    }

    @Override
    public void undo() {
        MobTickRegistry.removeEntityEffects(ticket);
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
