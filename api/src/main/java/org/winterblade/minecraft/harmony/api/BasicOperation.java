package org.winterblade.minecraft.harmony.api;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptContext;

/**
 * Basic class that should be the root of all @Operations.
 *
 * You should implement at a minimum 'apply'; 'init' and 'undo' are optional, though
 * generally speaking, all operations should be undoable unless that's impossible (or at least not within reason).
 *
 */
public abstract class BasicOperation implements IOperation {
    private int priority;
    private String id;
    private transient boolean applied;

    // State tracking
    private transient OperationState _operationState = OperationState.New;

    // Add some properties for metadata:
    protected String __comment;
    protected String __result;
    protected String __name;

    @Override
    public int compareTo(IOperation o) {
        // Don't try and sort:
        if(!(BasicOperation.class.isAssignableFrom(o.getClass()))) return 0;

        return priority - ((BasicOperation) o).priority;
    }

    /**
     * Populates the operation out of the data provided.
     * @param data  The operation data.
     * @return      If the operation succeeded.
     */
    public final boolean convert(IScriptContext context, ScriptObjectMirror data) {
        try {
            readData(context, data);
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
    protected void readData(IScriptContext context, ScriptObjectMirror data) throws OperationException {
        // Base implementation just attempts to map properties one-to-one
        context.parseScriptObject(data, this);
    }

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public final void runInit() throws OperationException {
        try {
            init();
            _operationState = OperationState.Initialized;
        } catch (Exception ex) {
            _operationState = OperationState.Errored;
            throw ex;
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public final void runApply() {
        // If we errored out, stop trying to apply...
        if(_operationState == OperationState.Errored) return;

        try {
            apply();
            _operationState = OperationState.Applied;
        } catch (Exception ex) {
            _operationState = OperationState.Errored;
            throw ex;
        }
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public final void runUndo() {
        // If we errored out, don't even bother trying to undo...
        if(_operationState == OperationState.Errored) return;

        try {
            undo();
            _operationState = OperationState.Initialized;
        } catch (Exception ex) {
            _operationState = OperationState.Errored;
            throw ex;
        }
    }

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    public void init() throws OperationException {
        // Do nothing
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    public void apply() {
        // Do nothing
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    public void undo() {
        // Do nothing
    }

    /**
     * If the set should only be allowable in a base set (default/difficulty based sets)
     *
     * This will log an error if the user tries to use the operation in a set other than the given sets.
     *
     * @return  True if base-only, false otherwise
     */
    @Override
    public boolean baseSetOnly() {
        return false;
    }

    /**
     * If the set should only be applied once
     * @return  True if it should only be done once; false otherwise
     */
    @Override
    public boolean onceOnly() {
        return false;
    }

    /**
     * Gets the ID for this operation.
     * @return  The ID for this command; should be unique
     */
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

    private static enum OperationState {
        New,
        Initialized,
        Applied,
        Errored
    }
}

