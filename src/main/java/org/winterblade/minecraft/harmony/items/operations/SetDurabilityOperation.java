package org.winterblade.minecraft.harmony.items.operations;

import net.minecraft.item.Item;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 6/30/2016.
 */
@Operation(name = "setItemDurability")
public class SetDurabilityOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private Item what;
    private int durability;

    /*
     * Computed properties
     */
    private transient int oldDurability;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null) throw new OperationException("Unable to find a proper input item for 'setItemDurability'.");
        if(durability < 0) throw new OperationException("The durability of an item cannot be set to less than 0.");
        oldDurability = what.getMaxDamage();
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        what.setMaxDamage(durability);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        what.setMaxDamage(oldDurability);
    }
}
