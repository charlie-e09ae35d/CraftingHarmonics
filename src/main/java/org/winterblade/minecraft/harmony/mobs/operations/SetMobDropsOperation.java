package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/4/2016.
 */
@Operation(name = "setMobDrops")
public class SetMobDropsOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String[] what;
    private boolean replace;
    private MobDrop[] drops;
    private ItemStack[] exclude;
    private ItemStack[] remove;
    private boolean includePlayerDrops;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = MobDropRegistry.registerHandler(what, drops, replace, exclude, remove, includePlayerDrops);
    }

    @Override
    public void apply() {
        LogHelper.info("Modifying drops for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobDropRegistry.apply(ticket);
    }

    @Override
    public void undo() {
        MobDropRegistry.remove(ticket);
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
