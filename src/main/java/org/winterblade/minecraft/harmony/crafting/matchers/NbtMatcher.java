package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.common.ItemUtility;

/**
 * Created by Matt on 4/9/2016.
 */
@Component(properties = {"nbt", "fuzzyNbt"})
@PrioritizedObject(priority = Priority.HIGHER)
public class NbtMatcher implements IRecipeInputMatcher {
    private final NBTTagCompound tag;
    private final boolean fuzzy;

    public NbtMatcher(NBTTagCompound nbt) {
        this(nbt, false);
    }

    public NbtMatcher(NBTTagCompound nbt, boolean fuzzyNbt) {
        this.tag = nbt;
        this.fuzzy = fuzzyNbt;
    }

    /**
     * Determine if the input ItemStack matches our target.  Do not modify any of the parameters; it will
     * only end in pain.  Or awesome.  But likely pain.
     *
     * @param input     The input from the crafting grid.
     * @param inventory The inventory performing the craft.
     * @param output    The output item of the recipe
     * @return True if the given input matches the target.
     */
    @Override
    public boolean matches(ItemStack input, InventoryCrafting inventory, ItemStack output) {
        return input != null && ItemUtility.CheckIfNbtMatches(tag, input.getTagCompound(), fuzzy);
    }
}
