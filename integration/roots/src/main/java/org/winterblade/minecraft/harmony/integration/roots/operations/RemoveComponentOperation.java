package org.winterblade.minecraft.harmony.integration.roots.operations;

import elucent.roots.component.ComponentManager;
import elucent.roots.component.ComponentRecipe;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 7/30/2016.
 */
@Operation(name = "Roots.removeDust", aliases = {"Roots.disableDust"}, dependsOn = "roots")
public class RemoveComponentOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String what;

    /*
     * Computed properties
     */
    private transient ComponentRecipe component;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        component = ComponentManager.getRecipe(what);
        if(component == null) throw new OperationException("Roots.removeDust cannot find dust matching '" + what + "'.");
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Disabling Roots dust '{}'.", what);
        component.disabled = true;
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        component.disabled = false;
    }
}
