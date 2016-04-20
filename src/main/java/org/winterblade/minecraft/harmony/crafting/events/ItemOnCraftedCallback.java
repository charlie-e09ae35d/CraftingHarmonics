package org.winterblade.minecraft.harmony.crafting.events;

/**
 * Created by Matt on 4/13/2016.
 */
@FunctionalInterface
public interface ItemOnCraftedCallback {
    public void apply(ItemOnCraftedEvent event);
}
