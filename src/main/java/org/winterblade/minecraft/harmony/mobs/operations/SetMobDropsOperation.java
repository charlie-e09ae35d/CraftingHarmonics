package org.winterblade.minecraft.harmony.mobs.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.dto.MobDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;

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

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void Init() throws ItemMissingException {
        ticket = MobDropRegistry.registerHandler(what, drops, replace, exclude);
    }

    @Override
    public void Apply() {
        LogHelper.info("Modifying drops for " + Joiner.on(", ").join(what));
        MobDropRegistry.apply(ticket);
    }

    @Override
    public void Undo() {
        MobDropRegistry.remove(ticket);
    }
}
