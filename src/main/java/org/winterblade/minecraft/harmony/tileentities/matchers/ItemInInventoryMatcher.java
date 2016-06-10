package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.winterblade.minecraft.harmony.api.*;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseInventoryMatcher;

/**
 * Created by Matt on 6/3/2016.
 */
@Component(properties = {"itemInInventory"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class ItemInInventoryMatcher extends BaseInventoryMatcher implements ITileEntityMatcher {
    private static final ItemStack dummy = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("minecraft:cobblestone")), 1);
    private final TileEntityInventoryData data;

    public ItemInInventoryMatcher(TileEntityInventoryData data) {
        super(data.what, data.damagePer, 0 < data.consume, data.nbt, data.fuzzyNbt, null);
        this.data = data;
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        dummy.stackSize = data.consume;
        return matches(tileEntity, dummy, data.minSlot, data.maxSlot, data.partial);
    }

    public static class TileEntityInventoryData {
        private ItemStack what;
        private double damagePer;
        private int consume;
        private boolean partial;
        private NBTTagCompound nbt;
        private boolean fuzzyNbt;
        private int minSlot = 0;
        private int maxSlot = Integer.MAX_VALUE;
    }
}
