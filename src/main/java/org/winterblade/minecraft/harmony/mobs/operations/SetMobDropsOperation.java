package org.winterblade.minecraft.harmony.mobs.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.mobs.MobDropRegistry;
import org.winterblade.minecraft.harmony.mobs.dto.MobDrop;

import java.util.UUID;

/**
 * Created by Matt on 5/4/2016.
 */
@RecipeOperation(name = "setMobDrops")
public class SetMobDropsOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private String what;
    private boolean replace;
    private MobDrop[] drops;
    private ItemStack[] exclude;
    private boolean playerOnly;

    /*
     * Computed properties
     */
    private transient UUID ticket;

    @Override
    public void Init() throws ItemMissingException {
        ticket = MobDropRegistry.registerHandler(what, drops, replace, exclude, playerOnly);
    }

    @Override
    public void Apply() {
        MobDropRegistry.apply(ticket);
    }

    @Override
    public void Undo() {
        MobDropRegistry.remove(ticket);
    }
}
