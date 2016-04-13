package org.winterblade.minecraft.harmony.crafting.events;

import org.winterblade.minecraft.harmony.crafting.events.wrappers.ItemStackWrapper;

import java.util.Random;

public class ItemOnCraftedEvent extends BaseEvent {
    private final ItemStackWrapper item;

    public ItemOnCraftedEvent(Random random, ItemStackWrapper item) {
        super(random);
        this.item = item;
    }

    public ItemStackWrapper getItem() {
        return item;
    }
}
