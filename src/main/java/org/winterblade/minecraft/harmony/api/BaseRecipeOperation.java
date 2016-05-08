package org.winterblade.minecraft.harmony.api;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;
import org.winterblade.minecraft.harmony.utility.LogHelper;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseRecipeOperation implements IRecipeOperation {
    private int priority;

    // Add some properties for metadata:
    protected String __comment;
    protected String __result;
    protected String __name;

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
            LogHelper.error("Error creating " + getClass().getSimpleName());
            return false;
        }
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     * @param data The operation data
     */
    protected void ReadData(ScriptObjectMirror data) throws ItemMissingException {
        // Base implementation just attempts to map properties one-to-one
        NashornConfigProcessor.getInstance().nashorn.parseScriptObject(data, this);
    }

    @Override
    public String toString() {
        if(__name != null) return __name;
        if(__result != null) return  __result;
        if(__comment != null) return __comment;
        return getClass().getSimpleName();
    }
}
