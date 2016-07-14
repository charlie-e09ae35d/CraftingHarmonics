package org.winterblade.minecraft.harmony.entities.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.entities.EntityInteractionData;
import org.winterblade.minecraft.harmony.entities.EntityInteractionHandler;
import org.winterblade.minecraft.harmony.entities.EntityRegistry;

import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 7/9/2016.
 */
@Operation(name = "addEntityInteraction")
public class AddEntityInteractionHandlerOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String[] what;
    private EntityInteractionData[] interactions;
    private boolean defaultDeny;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(interactions == null || interactions.length <= 0) throw new OperationException("addEntityInteraction operation must have at least one interaction defined.");
        if(what == null) what = new String[0];
        ticket = EntityRegistry.addInteractionHandler(new EntityInteractionHandler(what, interactions, defaultDeny));
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        EntityRegistry.activateInteractionHandler(ticket);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        EntityRegistry.deactivateInteractionHandler(ticket);
    }
}
