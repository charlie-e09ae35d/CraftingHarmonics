package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.harmony.api.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/4/2016.
 */
@RecipeOperation(name = "setMobDrops")
public class SetMobDropsOperation extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        ticket = MobDropRegistry.registerHandler(what, drops, replace, exclude, remove, includePlayerDrops);
    }

    @Override
    public void Apply() {
        LogHelper.info("Modifying drops for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        MobDropRegistry.apply(ticket);
    }

    @Override
    public void Undo() {
        MobDropRegistry.remove(ticket);
    }
}
