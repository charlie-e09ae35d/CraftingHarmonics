package org.winterblade.minecraft.harmony.crafting.events;

import org.winterblade.minecraft.harmony.crafting.events.wrappers.ItemStackWrapper;

import java.util.Random;

public class ItemOnCraftedEvent {
    private final ItemStackWrapper item;

    public ItemOnCraftedEvent(ItemStackWrapper item) {
        this.item = item;
    }

    public ItemStackWrapper getItem() {
        return item;
    }
}
