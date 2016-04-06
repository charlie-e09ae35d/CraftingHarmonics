package org.winterblade.minecraft.harmony.crafting.operations;

import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseAddOperation extends BaseRecipeOperation {
    /**
     * Serialized properties:
     */
    protected String output;
    protected int quantity;

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        return output.compareTo(((BaseAddOperation) o).output);
    }
}
