package org.winterblade.minecraft.harmony.items.operations;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.BasePerPlayerOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/17/2016.
 */
@Operation(name = "addStarterItem")
public class AddStarterItemOperation extends BasePerPlayerOperation {
    /*
     * Serialized properties
     */
    private ItemStack[] what;

    /*
     * Computed properties
     */
    private transient String id;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void doInit() throws OperationException {
        if(what == null) throw new OperationException("addStarterItems must have a list of starter items.");

        // Only compute an ID if necessary...
        if(!super.getId().equals("")) return;
        id = "addStarterItems";
        for(ItemStack item : what) {
            id += "_" + ItemUtility.outputItemName(item);
        }
    }

    @Override
    public void applyPerPlayer(EntityPlayerMP player) {
        LogHelper.info("Adding starter set of items for player " + player.getName());
        for(ItemStack item : what) {
            player.inventory.addItemStackToInventory(item.copy());
        }
    }

    @Override
    public void undoPerPlayer(EntityPlayerMP player) {
        // Can't be undone.
    }

    @Override
    public boolean baseSetOnly() {
        return true;
    }

    @Override
    public boolean onceOnly() {
        return true;
    }

    @Override
    public String getId() {
        String actualId = super.getId();
        return !actualId.equals("") ? actualId : id;
    }

    /**
     * Called to check if the operation should be applied.
     *
     * @return True if the operation should execute now; false otherwise.
     */
    @Override
    public boolean shouldUndo() {
        return false;
    }
}
