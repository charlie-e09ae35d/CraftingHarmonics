package org.winterblade.minecraft.harmony.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
public class ItemRegistry {
    private static final BiMap<String, Item> itemsByFullyQualifiedName = HashBiMap.create();
    private static final Map<String, ArrayList<Item>> itemsByMod = new HashMap<String, ArrayList<Item>>();

    public static void Init() {
        // Get the item's location
        System.out.println("Initializing item registry...");
        for (ResourceLocation itemResource : Item.itemRegistry.getKeys()) {
            String mod = itemResource.getResourceDomain();
            String itemName = itemResource.getResourcePath();
            String name = mod + ":" + itemName;
            Item item = Item.itemRegistry.getObject(itemResource);

            // Store it by its full name:
//            System.out.println("Registering '" + name + "' in item registry.");
            itemsByFullyQualifiedName.put(name, item);

            // Also store it by its mod:
            if(!itemsByMod.containsKey(mod)) itemsByMod.put(mod, new ArrayList<Item>());
            itemsByMod.get(mod).add(item);
        }
        System.out.println("Item registry complete.");
    }

    /**
     * Searches for an item's full name (with mod ID)
     * @param item  The item to search for
     * @return      The fully qualified name
     */
    public static String GetFullyQualifiedItemName(Item item) {
        return itemsByFullyQualifiedName.inverse().get(item);
    }

    /**
     * Gets an ItemStack with the item
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack GetItem(String fullyQualifiedName) throws ItemMissingException {
        return GetItem(fullyQualifiedName, 1);
    }

    /**
     * Gets an ItemStack with the item and quantity requested.
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack GetItem(String fullyQualifiedName, int quantity) throws ItemMissingException {
        return GetItem(fullyQualifiedName, 1, 0);
    }

    /**
     * Gets an ItemStack with the item, quantity, and metadata requested
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack GetItem(String fullyQualifiedName, int quantity, int meta) throws ItemMissingException {
        if(!itemsByFullyQualifiedName.containsKey(fullyQualifiedName)) throw new ItemMissingException("Unable to find item named " + fullyQualifiedName);

        return new ItemStack(itemsByFullyQualifiedName.get(fullyQualifiedName), quantity, meta);
    }

    /**
     * Translates an item input into an appropriate item stack
     * @param item  The item to translate
     * @return      The ItemStack requested
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack TranslateToItemStack(String item) throws ItemMissingException {
        return TranslateToItemStack(item, 1);
    }

    /**
     * Translates an item input into an appropriate item stack
     * @param item      The item to translate
     * @param quantity  The quantity requested
     * @return          The ItemStack requested
     * @throws ItemMissingException When the item cannot be found in the registry.
     */
    public static ItemStack TranslateToItemStack(String item, int quantity ) throws ItemMissingException {
        if(item.equals("")) return null;

        String[] parts = item.split(":");

        if(parts.length == 1) {
            return GetItem("minecraft:" + parts[1], quantity);
        } else if(parts.length == 2) {
            return GetItem(parts[0] + ":" + parts[1], quantity);
        } else if(parts.length >= 3) {
            return GetItem(parts[0] + ":" + parts[1], quantity, Integer.parseInt(parts[2]));
        }

        return null;
    }
}
