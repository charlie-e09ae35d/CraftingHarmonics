package org.winterblade.minecraft.harmony.api;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
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

    /**
     * Populates the operation out of the data provided.
     * @param data  The operation data.
     * @return      If the operation succeeded.
     */
    public final boolean Convert(ScriptObjectMirror data) {
        try {
            ReadData(data);
            return true;
        }
        catch(Exception e) {
            System.out.println("Error creating " + getClass().getSimpleName());
            return false;
        }
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     * @param data The operation data
     */
    protected abstract void ReadData(ScriptObjectMirror data);
}
