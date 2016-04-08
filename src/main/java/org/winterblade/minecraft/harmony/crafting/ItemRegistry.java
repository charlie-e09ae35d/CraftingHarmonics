package org.winterblade.minecraft.harmony.crafting;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
public class ItemRegistry {
    private static final BiMap<String, Item> itemsByFullyQualifiedName = HashBiMap.create();
    private static final Map<String, ArrayList<Item>> itemsByMod = new HashMap<>();

    public static void Init() {
        // Get the item's location
        System.out.println("Initializing item registry...");

        RegistryNamespaced<ResourceLocation, Item> itemRegistry = Item.itemRegistry;

        for (ResourceLocation itemResource : itemRegistry.getKeys()) {
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
        return GetItem(fullyQualifiedName, quantity, 0);
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
    public static ItemStack TranslateToItemStack(String item, int quantity) throws ItemMissingException {
        if(item == null || item.equals("")) return null;

        ItemType translatedType = ItemType.Regular;

        String[] parts = item.split(":");

        // Check if we're reading in NBT data...
        if(parts.length >= 5) {
            if(parts[4].startsWith("=")) translatedType = ItemType.ExactNbt;
            else if(parts[4].startsWith("~")) translatedType = ItemType.FuzzyNbt;

            parts[4] = parts[4].substring(1);

            // Now recombine the NBT; if only I hadn't used : as a separator...
            for(int i = 5; i < parts.length; i++) {
                parts[4] += ":" + parts[i];
            }
        }

        ItemStack itemStack = null;

        if(parts.length == 1) {
            itemStack = GetItem("minecraft:" + parts[0], 1);
        } else if(parts.length == 2) {
            itemStack = GetItem(parts[0] + ":" + parts[1], 1);
        } else if (parts.length == 3 && parts[2].equals("*")) {
            itemStack = GetItem(parts[0] + ":" + parts[1], 1, OreDictionary.WILDCARD_VALUE);
        } else if(parts.length == 3) {
            itemStack = GetItem(parts[0] + ":" + parts[1], 1, Integer.parseInt(parts[2]));
        } else if(parts.length >= 4) {
            itemStack = parts[2].equals("*")
                ? GetItem(parts[0] + ":" + parts[1], Integer.parseInt(parts[3]))
                : GetItem(parts[0] + ":" + parts[1], Integer.parseInt(parts[3]), Integer.parseInt(parts[2]));
        }

        if(itemStack == null) return null;

        // Set our stack size if a valid quantity was specified (and larger than one):
        if(1 < quantity && quantity <= itemStack.getMaxStackSize()) {
            itemStack.stackSize = quantity;
        }

        // Check to see if we're doing anything special here...
        if(translatedType != ItemType.Regular) {
            NBTTagCompound compound;

            // Get the compound...
            try {
                compound = JsonToNBT.getTagFromJson(parts[4]);
            } catch (NBTException e) {
                throw new ItemMissingException("Unable to convert input NBT into something readable by Minecraft; got response '" + e.getMessage() + "'.");
            }

            // Update it with our values:
            compound.setBoolean("CraftingHarmonicsIsFuzzyMatch", translatedType == ItemType.FuzzyNbt);
            itemStack.setTagCompound(compound);
        }

        return itemStack;
    }

    /**
     * Checks if the given item stacks are equivalent
     * @param a The first item stack
     * @param b The second item stack
     * @return  If the items, stack size, and metadata are equivalent.
     */
    public static boolean AreItemsEquivalent(ItemStack a, ItemStack b) {
        return AreItemsEquivalent(a.getItem(), b.getItem())
                && (a.getMetadata() == OreDictionary.WILDCARD_VALUE || a.getMetadata() == b.getMetadata());
    }

    /**
     * Checks if the given items are equivalent.
     * @param a The first item
     * @param b The second item
     * @return  If the items' fully qualified names are equal.
     */
    public static boolean AreItemsEquivalent(Item a, Item b) {
        String aName = GetFullyQualifiedItemName(a);
        String bName = GetFullyQualifiedItemName(b);

        return (aName.equals(bName));
    }

    /**
     * Checks to see if a given entry is an ore dictionary name or an item.
     * @param entry  The entry to check
     * @return      True if this is an ore dictionary item.
     */
    public static boolean IsOreDictionaryEntry(String entry) {
        return entry.startsWith("<");
    }

    /**
     * Gets the ore dictionary name for an entry
     * @param entry The entry to check
     * @return      The ore dictionary name
     */
    public static String GetOreDictionaryName(String entry) {
        return entry.substring(1,entry.length()-1);
    }

    private enum ItemType {
        Regular, ExactNbt, FuzzyNbt
    }
}
