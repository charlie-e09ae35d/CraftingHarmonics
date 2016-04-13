package org.winterblade.minecraft.harmony.crafting.events;

import jdk.nashorn.api.scripting.JSObject;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

/**
 * Created by Matt on 4/13/2016.
 */
@FunctionalInterface
public interface ItemOnCraftedCallback {
    public void apply(ItemOnCraftedEvent event);
}
