package org.winterblade.minecraft.harmony.api;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptContext;

import javax.annotation.Nullable;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseRecipeOperation implements IRecipeOperation {
    private int priority;
    private String id;
    private transient boolean applied;

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
    public final boolean Convert(IScriptContext context, ScriptObjectMirror data) {
        try {
            ReadData(context, data);
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
    protected void ReadData(IScriptContext context, ScriptObjectMirror data) throws ItemMissingException {
        // Base implementation just attempts to map properties one-to-one
        context.parseScriptObject(data, this);
    }

    /**
     * Called to initialize the set
     *
     * @throws ItemMissingException If something went wrong
     */
    @Override
    public void Init() throws ItemMissingException {
        // Do nothing
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void Apply() {
        // Do nothing
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void Undo() {
        // Do nothing
    }

    @Override
    public boolean baseSetOnly() {
        return false;
    }

    @Override
    public boolean onceOnly() {
        return false;
    }

    @Override
    public boolean perPlayer() {
        return false;
    }

    @Override
    @Nullable
    public Side getSide() {
        return null;
    }

    @Override
    public String getId() {
        return id != null ? id : "";
    }

    /**
     * Called to check if the operation should be applied.
     *
     * @return True if the operation should execute now; false otherwise.
     */
    @Override
    public boolean shouldApply() {
        if(!onceOnly()) return true;
        if(applied) return false;

        applied = true;
        return true;
    }

    /**
     * Called to check if the operation should be applied.
     *
     * @return True if the operation should execute now; false otherwise.
     */
    @Override
    public boolean shouldUndo() {
        // If we're once-only, then we don't want to undo...
        return !onceOnly();
    }


    @Override
    public String toString() {
        if(__name != null) return __name;
        if(__result != null) return  __result;
        if(__comment != null) return __comment;
        return getClass().getSimpleName();
    }
}

