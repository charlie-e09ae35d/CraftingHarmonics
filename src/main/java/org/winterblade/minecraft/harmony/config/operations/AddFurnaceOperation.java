package org.winterblade.minecraft.harmony.config.operations;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.Map;

/**
 * Created by Matt on 4/5/2016.
 */
public class AddFurnaceOperation implements IConfigOperation {
    /**
     * Serialized properties:
     */
    private String output;
    private int quantity;
    private String with;

    /**
     * Actual items and whatnot
     */
    private transient ItemStack inputItem;
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        outputItemStack = ItemRegistry.TranslateToItemStack(output);
        if(outputItemStack == null) throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        if(1 < quantity && quantity <= 64) {
            outputItemStack.stackSize = quantity;
        }

        inputItem = ItemRegistry.TranslateToItemStack(with);
        if(inputItem == null) throw new RuntimeException("Unable to find requested input item '" + with + "'.");
    }

    public Map.Entry<ItemStack, ItemStack> CreateRecipe() {
        return Maps.immutableEntry(inputItem, outputItemStack);
    }
}
