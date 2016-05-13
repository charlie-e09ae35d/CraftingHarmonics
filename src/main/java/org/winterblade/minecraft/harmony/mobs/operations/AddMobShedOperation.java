package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.mobs.MobShedRegistry;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;
import org.winterblade.minecraft.harmony.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/10/2016.
 */
@RecipeOperation(name = "addMobShed")
public class AddMobShedOperation extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        ticket = MobShedRegistry.registerHandler(what, sheds);
    }

    @Override
    public void Apply() {
        LogHelper.info("Adding sheds for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobShedRegistry.apply(ticket);
    }

    @Override
    public void Undo() {
        MobShedRegistry.remove(ticket);
    }
}
