package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/6/2016.
 */
@RecipeOperation(name = "registerOreDictItem")
public class RegisterOreDictItemOperation extends BaseRecipeOperation {
    /**
     * Serialized properties
     */
    RecipeComponent what;
    String oreDict;

    @Override
    public void Init() throws ItemMissingException {
        if (what == null)
            throw new RuntimeException("Unable to find item " + what.toString() + " to add to dictionary '" + oreDict + "'.");
    }

    @Override
    public void Apply() {
        System.out.println("Adding '" + what.toString() + "' to the dictionary '" + oreDict + "'.");
        OreDictionary.registerOre(oreDict, what.getItemStack());
    }

    @Override
    public void Undo() {

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
        return what.getItemStack().getUnlocalizedName().compareTo(other.what.getItemStack().getUnlocalizedName());
    }
}
