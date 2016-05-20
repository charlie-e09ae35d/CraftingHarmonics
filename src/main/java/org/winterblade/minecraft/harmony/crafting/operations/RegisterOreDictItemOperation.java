package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/6/2016.
 */
@Operation(name = "registerOreDictItem")
public class RegisterOreDictItemOperation extends BasicOperation {
    /**
     * Serialized properties
     */
    RecipeComponent what;
    String oreDict;

    @Override
    public void init() throws OperationException {
        if (what == null)
            throw new OperationException("Unable to find item " + what.toString() + " to add to dictionary '" + oreDict + "'.");
    }

    @Override
    public void apply() {
        LogHelper.info("Adding '" + what.toString() + "' to the dictionary '" + oreDict + "'.");
        OreDictionary.registerOre(oreDict, what.getItemStack());
    }

    @Override
    public void undo() {
        // Nope.
        // I mean, I could, but, it'd probably be a bad idea.
    }

    @Override
    public int compareTo(IOperation o) {
        int baseCompare = super.compareTo(o);
        if (baseCompare != 0) return baseCompare;

        // After removes...
        if ((o instanceof RemoveOperation)) return 1;

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
