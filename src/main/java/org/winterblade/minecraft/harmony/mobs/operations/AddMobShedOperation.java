package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/10/2016.
 */
@Operation(name = "addMobShed")
public class AddMobShedOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String[] what;
    private MobShed[] sheds;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = MobTickRegistry.registerShed(what, sheds);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding sheds for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobTickRegistry.applyShed(ticket);
    }

    @Override
    public void undo() {
        MobTickRegistry.removeShed(ticket);
    }
}
