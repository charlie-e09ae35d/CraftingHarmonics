package org.winterblade.minecraft.harmony.integration.jei.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.jei.Jei;

/**
 * Created by Matt on 5/6/2016.
 */
@Operation(name = "hide", dependsOn = "JEI")
public class HideOperation extends BasicOperation {
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
        Jei.hide(what);
    }

    @Override
    public void undo() {
        Jei.show(what);
    }
}
