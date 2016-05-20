package org.winterblade.minecraft.harmony.common;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.OperationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/19/2016.
 */
public class ItemUtility {
    /**
     * Searches for an item's full name (with mod ID)
     * @param item  The item to search for
     * @return      The fully qualified name
     */
    public static String getFullyQualifiedItemName(Item item) {
        return Item.REGISTRY.getNameForObject(item).toString();
    }

    /**
     * Gets an ItemStack with the item
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws OperationException When the item cannot be found in the registry.
     */
    public static ItemStack getItem(String fullyQualifiedName) throws OperationException {
        return getItem(fullyQualifiedName, 1);
    }

    /**
     * Gets an ItemStack with the item and quantity requested.
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws OperationException When the item cannot be found in the registry.
     */
    public static ItemStack getItem(String fullyQualifiedName, int quantity) throws OperationException {
        return getItem(fullyQualifiedName, quantity, 0);
    }

    /**
     * Gets an ItemStack with the item, quantity, and metadata requested
     * @param fullyQualifiedName    The FQIN
     * @return                      The item
     * @throws OperationException When the item cannot be found in the registry.
     */
    public static ItemStack getItem(String fullyQualifiedName, int quantity, int meta) throws OperationException {
        Item item = Item.REGISTRY.getObject(new ResourceLocation(fullyQualifiedName));
        if(item == null) throw new OperationException("Item '" + fullyQualifiedName + "' could not be found.");
        return new ItemStack(item, quantity, meta);
    }

    /**
     * Translates an item input into an appropriate item stack
     * @param item  The item to translate
     * @return      The ItemStack requested
     * @throws OperationException When the item cannot be found in the registry.
     */
    public static ItemStack translateToItemStack(String item) throws OperationException {
        return translateToItemStack(item, 1);
    }

    /**
     * Translates an item input into an appropriate item stack
     * @param item      The item to translate
     * @param quantity  The quantity requested
     * @return          The ItemStack requested
     * @throws OperationException When the item cannot be found in the registry.
     */
    public static ItemStack translateToItemStack(String item, int quantity) throws OperationException {
        if(item == null || item.equals("")) return null;

        ItemType translatedType = ItemType.Regular;

        String[] parts = item.split(":");

        // Check if we're reading in NBT data...
        if(parts.length >= 5) {
            if(parts[4].startsWith("=") || parts[4].startsWith("{")) translatedType = ItemType.ExactNbt;
            else if(parts[4].startsWith("~")) translatedType = ItemType.FuzzyNbt;

            if(!parts[4].startsWith("{")) parts[4] = parts[4].substring(1);

            // Now recombine the NBT; if only I hadn't used : as a separator...
            for(int i = 5; i < parts.length; i++) {
                parts[4] += ":" + parts[i];
            }
        }

        ItemStack itemStack = null;

        if(parts.length == 1) {
            itemStack = getItem("minecraft:" + parts[0], 1);
        } else if(parts.length == 2) {
            itemStack = getItem(parts[0] + ":" + parts[1], 1);
        } else if (parts.length == 3 && parts[2].equals("*")) {
            itemStack = getItem(parts[0] + ":" + parts[1], 1, OreDictionary.WILDCARD_VALUE);
        } else if(parts.length == 3) {
            itemStack = getItem(parts[0] + ":" + parts[1], 1, Integer.parseInt(parts[2]));
        } else if(parts.length >= 4) {
            itemStack = parts[2].equals("*")
                ? getItem(parts[0] + ":" + parts[1], Integer.parseInt(parts[3]))
                : getItem(parts[0] + ":" + parts[1], Integer.parseInt(parts[3]), Integer.parseInt(parts[2]));
        }

        if(itemStack == null) return null;
        updateStackQuantity(itemStack, quantity);


        // Check to see if we're doing anything special here...
        if(translatedType != ItemType.Regular) {
            NBTTagCompound compound;

            // Get the compound...
            try {
                compound = JsonToNBT.getTagFromJson(parts[4]);
            } catch (NBTException e) {
                throw new OperationException("Unable to convert input NBT into something readable by Minecraft; got response '" + e.getMessage() + "'.");
            }

            // Update it with our values:
            if(translatedType == ItemType.FuzzyNbt) compound.setBoolean("CraftingHarmonicsIsFuzzyMatch", true);
            itemStack.setTagCompound(compound);
        }

        return itemStack;
    }

    /**
     * Updates the quantity of a stack if the quantity is within acceptable ranges.
     * @param itemStack The item stack
     * @param quantity  The quantity to set.
     */
    public static void updateStackQuantity(ItemStack itemStack, int quantity) {
        // Set our stack size if a valid quantity was specified (and larger than one):
        if(1 < quantity && quantity <= itemStack.getMaxStackSize()) {
            itemStack.stackSize = quantity;
        }
    }

    /**
     * Checks if the given item stacks are equivalent
     * @param a The first item stack
     * @param b The second item stack
     * @return  If the items, stack size, and metadata are equivalent.
     */
    public static boolean areItemsEquivalent(ItemStack a, ItemStack b) {
        return areItemsEquivalent(a.getItem(), b.getItem())
                && (a.getMetadata() == OreDictionary.WILDCARD_VALUE || a.getMetadata() == b.getMetadata());
    }

    /**
     * Checks if the given items are equivalent.
     * @param a The first item
     * @param b The second item
     * @return  If the items' fully qualified names are equal.
     */
    public static boolean areItemsEquivalent(Item a, Item b) {
        String aName = getFullyQualifiedItemName(a);
        String bName = getFullyQualifiedItemName(b);

        return (aName.equals(bName));
    }

    /**
     * Checks to see if a given entry is an ore dictionary name or an item.
     * @param entry  The entry to check
     * @return      True if this is an ore dictionary item.
     */
    public static boolean isOreDictionaryEntry(String entry) {
        return entry.startsWith("<");
    }

    /**
     * Gets the ore dictionary name for an entry
     * @param entry The entry to check
     * @return      The ore dictionary name
     */
    public static String getOreDictionaryName(String entry) {
        return entry.substring(1,entry.length()-1);
    }

    /**
     * Checks to see if the NBT on the first item stack matches the second
     * @param source    The source to check
     * @param dest      The destination to check
     * @param isFuzzy   If we should force a fuzzy match
     * @return          True if they are the same (or similar if it's a fuzzy match)
     */
    public static boolean checkIfNbtMatches(ItemStack source, ItemStack dest, boolean isFuzzy) {
        return source != null && dest != null
                && source.hasTagCompound() == dest.hasTagCompound()
                && checkIfNbtMatches(source.getTagCompound(), dest.getTagCompound(), isFuzzy);
    }

    /**
     * Checks to see if the NBT on the first item stack matches the second
     * @param orig      The source to check
     * @param dest      The destination to check
     * @param isFuzzy   If we should force a fuzzy match
     * @return          True if they are the same (or similar if it's a fuzzy match)
     */
    public static boolean checkIfNbtMatches(NBTTagCompound orig, NBTTagCompound dest, boolean isFuzzy) {
        // If one or the other is null, and they both aren't, bail
        if(dest == null && orig != null) return false;

        // Also, if we're checking two items that don't have a compound, then it's 'true'-ish
        if(orig == null) return true;

        NBTTagCompound compound = new NBTTagCompound();

        // Fall back to checking the tag compound...
        if(!isFuzzy) isFuzzy = orig.hasKey("CraftingHarmonicsIsFuzzyMatch");

        for (String s : orig.getKeySet()) {
            // Skip our tag...
            if (s.equals("CraftingHarmonicsIsFuzzyMatch")) continue;
            compound.setTag(s, orig.getTag(s).copy());
        }

        // If we're not fuzzy, then...
        if (!isFuzzy) {
            // We need to check if the NBT matches exactly...
            return compound.toString().equals(dest.toString());
        }

        return checkIfAtLeastAllTagsArePresent(compound, dest);
    }

    /**
     * Checks if at least all tags in the first parameter are in the second
     * @param source    The source to check
     * @param dest      The destination to confirm there are at least the same tags in
     * @return          True if they are present in the second.
     */
    private static boolean checkIfAtLeastAllTagsArePresent(NBTTagCompound source, NBTTagCompound dest) {
        for(String s : source.getKeySet()) {
            // If we don't have it, bail...
            if(!dest.hasKey(s)) return false;

            NBTBase sourceTag = source.getTag(s);
            NBTBase destTag = dest.getTag(s);
            if (!compareTags(sourceTag, destTag)) return false;
        }

        return true;
    }

    /**
     * Checks if at least all tags in the first parameter are in the second
     * @param sourceTag    The source to check
     * @param destTag      The destination to check
     * @return             True if they are present in the second
     */
    private static boolean compareTags(NBTBase sourceTag, NBTBase destTag) {
        // If our tags aren't the same type
        if(sourceTag.getId() != destTag.getId()) return false;

        // If we have an NBTTagCompound
        if(sourceTag instanceof NBTTagCompound) {
            // Go deeper...
            if (!checkIfAtLeastAllTagsArePresent((NBTTagCompound) sourceTag, (NBTTagCompound) destTag))
                return false;
        } else if(sourceTag instanceof NBTTagList) {
            NBTTagList sourceList = (NBTTagList) sourceTag;
            NBTTagList destList = (NBTTagList) destTag;

            for(int i = 0; i < sourceList.tagCount(); i++) {
                if(!compareTags(sourceList.get(i), destList.get(i))) return false;
            }
        } else if(!sourceTag.equals(destTag)) return false;
        return true;
    }

    /**
     * Turns an inventory grid into a List
     * @param inv   The inventory grid to convert
     * @return      The grid as a List, skipping nulls
     */
    public static List<ItemStack> getInventoryGridAsList(InventoryCrafting inv) {
        List<ItemStack> grid = new ArrayList<>();

        // Get a better list of grid inputs...
        for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= 3; ++j) {
                // Get the item in that spot...
                ItemStack slot = inv.getStackInRowAndColumn(i, j);
                if (slot == null) continue;

                grid.add(slot);
            }
        }

        return grid;
    }

    public static String outputItemName(ItemStack itemStack) {
        if(itemStack == null) return "null";
        int meta = itemStack.getMetadata();
        return Item.REGISTRY.getNameForObject(itemStack.getItem()).toString() + ":" +
                ((meta == OreDictionary.WILDCARD_VALUE) ? "*" : meta);
    }

    /**
     * Simulates an inventory with the given inputs as
     * @param inputs    The inputs
     * @param width     The width of the inventory to simulate
     * @param height    The height of the inventory to simulate
     * @return          The crafting inventory
     */
    public static InventoryCrafting simulateInventoryOf(ItemStack[] inputs, int width, int height) {
        if(inputs == null) return null;

        // Make a container...
        Container c = new ContainerWorkbench(new InventoryPlayer(null), null, BlockPos.ORIGIN);

        // Figure out our sizes:
        if(width <= 0 && height <= 0) {
            width = height = inputs.length > 4 ? 3 : 2;
        }

        if(width <= 0) {
            width = (int)Math.ceil((double)inputs.length / height);
        }

        if(height <= 0) {
            height = (int)Math.ceil((double)inputs.length / width);
        }

        InventoryCrafting inv = new InventoryCrafting(c, width, height);

        for (int i = 0; i < inputs.length; i++) {
            inv.setInventorySlotContents(i, inputs[i]);
        }

        // And pretend we're crafting:
        return inv;
    }

    private enum ItemType {
        Regular, ExactNbt, FuzzyNbt
    }
}
