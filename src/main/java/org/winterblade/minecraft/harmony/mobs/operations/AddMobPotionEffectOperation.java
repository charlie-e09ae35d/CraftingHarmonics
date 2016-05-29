package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.entities.effects.MobPotionEffect;

import java.util.UUID;

/**
 * Created by Matt on 5/20/2016.
 */
@Operation(name = "addMobPotionEffect")
public class AddMobPotionEffectOperation extends BasicOperation {
 /*
 * Serialized properties
 */
    private String[] what;
    private MobPotionEffect[] effects;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void init() throws OperationException {
        ticket = MobTickRegistry.registerPotionEffect(what, effects);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding potion effects for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobTickRegistry.applyPotionEffect(ticket);
    }

    @Override
    public void undo() {
        MobTickRegistry.removePotionEffect(ticket);
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
