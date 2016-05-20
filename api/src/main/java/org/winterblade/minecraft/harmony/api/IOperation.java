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
    void init() throws OperationException;

    /**
     * Called to apply the set (if not player-specific)
     */
    void apply();

    /**
     * Called to remove the set (if not player-specific)
     */
    void undo();

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
     * If the set should be applied per player
     * @return  True if it should be applied per player; false otherwise
     */
    boolean perPlayer();

    /**
     * If the operation should only be applied client/server side
     * @return  The side to apply the operation on, null otherwise.
     */
    @Nullable
    Side getSide();

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
}
