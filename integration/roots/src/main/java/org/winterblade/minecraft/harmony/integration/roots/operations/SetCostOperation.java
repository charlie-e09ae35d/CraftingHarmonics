package org.winterblade.minecraft.harmony.integration.roots.operations;

import elucent.roots.component.ComponentBase;
import elucent.roots.component.ComponentManager;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 7/30/2016.
 */
@Operation(name = "Roots.setSpellCost", dependsOn = "roots")
public class SetCostOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String what;
    private int cost;

    /*
     * Computed properties
     */
    private transient ComponentBase component;
    private transient int oldCost;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        component = ComponentManager.getComponentFromName(what);
        if(component == null) throw new OperationException("Roots.setSpellCost cannot find dust matching '" + what + "'.");
        oldCost = component.xpCost;
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Setting cost of Roots spell '{}' to {}.", what, cost);
        component.xpCost = cost;
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        component.xpCost = oldCost;
    }
}
