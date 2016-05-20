package org.winterblade.minecraft.harmony.crafting.integration.jei.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.crafting.integration.jei.Jei;

/**
 * Created by Matt on 5/6/2016.
 */
@Operation(name = "hide", dependsOn = "JEI")
public class ShowOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack what;

    @Override
    public void init() throws OperationException {
        // Do nothing.
    }

    @Override
    public void apply() {
        Jei.show(what);
    }

    @Override
    public void undo() {
        Jei.hide(what);
    }
}
