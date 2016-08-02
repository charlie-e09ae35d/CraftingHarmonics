package org.winterblade.minecraft.harmony.integration.roots.operations;

import elucent.roots.ritual.RitualBase;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.roots.RootsRegistry;

/**
 * Created by Matt on 7/27/2016.
 */
@Operation(name = "Roots.removeRitual", dependsOn = "roots")
public class RemoveRitualOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String name;

    /*
     * Computed properties
     */
    RitualBase ritual;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        ritual = RootsRegistry.getRitual(name);

        if(ritual == null) throw new OperationException("removeRitual cannot find a ritual by name '" + name + "'.");
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Removing Roots ritual '{}'.", name);
        RootsRegistry.removeRitual(ritual);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        RootsRegistry.addRitual(ritual);
    }
}
