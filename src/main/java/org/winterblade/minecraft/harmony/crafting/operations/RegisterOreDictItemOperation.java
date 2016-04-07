package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "registerOreDictItem")
public class RegisterOreDictItemOperation extends BaseRecipeOperation {
    /**
     * Serialized properties
     */
    String what;
    String oreDict;

    /**
     * Computed properties
     */
    private transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException {
        outputItemStack = ItemRegistry.TranslateToItemStack(what);
        if (outputItemStack == null)
            throw new RuntimeException("Unable to find item '" + what + "' to add to dictionary '" + oreDict + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding '" + outputItemStack.getUnlocalizedName() + "' to the dictionary '" + oreDict + "'.");
        OreDictionary.registerOre(oreDict, outputItemStack);
    }

    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if (baseCompare != 0) return baseCompare;

        // After removes...
        if (!(o instanceof RemoveOperation)) return 1;

        // Before anything else...
        if (!(o instanceof RegisterOreDictItemOperation)) return -1;

        RegisterOreDictItemOperation other = (RegisterOreDictItemOperation) o;

        // Group the ore dict names
        int oreDictCompare = oreDict.compareTo(other.oreDict);
        if(oreDictCompare != 0) return oreDictCompare;

        // Otherwise, sort it by item
        return what.compareTo(other.oreDict);
    }
}
