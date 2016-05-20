package org.winterblade.minecraft.harmony.misc.operations;

import net.minecraft.item.Item;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;

/**
 * Created by Matt on 5/2/2016.
 */
@Operation(name = "setToolHarvestLevel")
public class SetToolLevelOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private Item what;
    private int to;
    private String as;

    /*
     * Computed properties
     */
    private transient int from;

    @Override
    public void init() throws OperationException {
        if(what == null) throw new OperationException("Cannot set tool harvest level on unknown item.");

        from = what.getHarvestLevel(null, as);
    }

    @Override
    public void apply() {
        what.setHarvestLevel(as, to);
    }

    @Override
    public void undo() {
        what.setHarvestLevel(as, from);
    }
}
