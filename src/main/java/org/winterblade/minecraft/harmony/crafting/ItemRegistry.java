package org.winterblade.minecraft.harmony.crafting;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
public class ItemRegistry {
    private static final Map<String, ArrayList<Item>> itemsByMod = new HashMap<>();

    public static void Init() {
        // Get the item's location
        LogHelper.info("Initializing item registry...");

        RegistryNamespaced<ResourceLocation, Item> itemRegistry = Item.REGISTRY;

        for (ResourceLocation itemResource : itemRegistry.getKeys()) {
            String mod = itemResource.getResourceDomain();
            Item item = Item.REGISTRY.getObject(itemResource);

            // Also store it by its mod:
            if(!itemsByMod.containsKey(mod)) itemsByMod.put(mod, new ArrayList<>());
            itemsByMod.get(mod).add(item);
        }
        LogHelper.info("Item registry complete.");
    }
}
