package org.winterblade.minecraft.harmony.blocks.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.blocks.BlockDropRegistry;
import org.winterblade.minecraft.harmony.common.blocks.BlockStateMatcher;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/12/2016.
 */
@Operation(name = "setBlockDrops")
public class SetBlockDropsOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String[] what;
    private boolean replace;
    private BlockDrop[] drops;
    private ItemStack[] exclude;
    private ItemStack[] remove;
    private BlockStateMatcher state;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = BlockDropRegistry.registerHandler(what, drops, replace, exclude, remove, state);
    }

    @Override
    public void apply() {
        LogHelper.info("Modifying block drops for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        BlockDropRegistry.apply(ticket);
    }

    @Override
    public void undo() {
        BlockDropRegistry.remove(ticket);
    }

    /**
     * Should the operation be initialized/applied/undone/etc on the client
     *
     * @return True if it should, false otherwise.  Defaults to true.
     */
    @Override
    public boolean isClientOperation() {
        return false;
    }
}