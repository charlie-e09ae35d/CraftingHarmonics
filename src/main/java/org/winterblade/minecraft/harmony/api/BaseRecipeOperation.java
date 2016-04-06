package org.winterblade.minecraft.harmony.api;

import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseRecipeOperation implements IRecipeOperation {
    private int priority;

    @Override
    public int compareTo(IRecipeOperation o) {
        // Don't try and sort:
        if(!(BaseRecipeOperation.class.isAssignableFrom(o.getClass()))) return 0;

        return priority - ((BaseRecipeOperation) o).priority;
    }
}
