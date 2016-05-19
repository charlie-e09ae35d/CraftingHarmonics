package org.winterblade.minecraft.harmony.blocks.operations;

import com.google.common.base.Joiner;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.blocks.BlockDropRegistry;
import org.winterblade.minecraft.harmony.blocks.BlockStateMatcher;
import org.winterblade.minecraft.harmony.blocks.drops.BlockDrop;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.UUID;

/**
 * Created by Matt on 5/12/2016.
 */
@RecipeOperation(name = "setBlockDrops")
public class SetBlockDropsOperation extends BaseRecipeOperation {
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
    public void Init() throws ItemMissingException {
        ticket = BlockDropRegistry.registerHandler(what, drops, replace, exclude, remove, state);
    }

    @Override
    public void Apply() {
        LogHelper.info("Modifying block drops for " + (0 < what.length ? Joiner.on(", ").join(what) : "everything"));
        BlockDropRegistry.apply(ticket);
    }

    @Override
    public void Undo() {
        BlockDropRegistry.remove(ticket);
    }
}