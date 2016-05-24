package org.winterblade.minecraft.harmony.api;

import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by Matt on 4/5/2016.
 */
public interface IOperation extends Comparable<IOperation> {
    /**
     * Called to initialize the set
     * @throws OperationException If something went wrong
     */
    void runInit() throws OperationException;

    /**
     * Called to apply the set (if not player-specific)
     */
    void runApply();

    /**
     * Called to remove the set (if not player-specific)
     */
    void runUndo();

    /**
     * If the set should only be allowable in a base set (default/difficulty based sets)
     * @return  True if base-only, false otherwise
     */
    boolean baseSetOnly();

    /**
     * If the set should only be applied once
     * @return  True if it should only be done once; false otherwise
     */
    boolean onceOnly();

    /**
     * Gets the ID for this operation.
     * @return  The ID for this command; should be unique
     */
    String getId();

    /**
     * Called to check if the operation should be applied.
     * @return  True if the operation should apply now; false otherwise.
     */
    boolean shouldApply();

    /**
     * Called to check if the operation should be undone.
     * @return  True if the operation should undo now; false otherwise.
     */
    boolean shouldUndo();

    /**
     * Should the operation be initialized/applied/undone/etc on the client
     * @return  True if it should, false otherwise.  Defaults to true.
     */
    boolean isClientOperation();

    /**
     * Should the operation be initialized/applied/undone/etc on the server
     * @return  True if it should, false otherwise.  Defaults to true.
     */
    boolean isServerOperation();
}
