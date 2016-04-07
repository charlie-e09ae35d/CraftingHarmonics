package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseAddOperation extends BaseRecipeOperation {
    /**
     * Serialized properties:
     */
    protected String output;
    protected int quantity;
    protected String displayName;

    /**
     * Computed properties
     */
    protected transient ItemStack outputItemStack;

    @Override
    public void Init() throws ItemMissingException
    {
        outputItemStack = ItemRegistry.TranslateToItemStack(output, quantity);
        if (outputItemStack == null)
            throw new RuntimeException("Unable to find requested output item '" + output + "'.");

        if(displayName != null && !displayName.equals("")) {
            outputItemStack.setStackDisplayName(displayName);
        }
    }


    @Override
    public int compareTo(IRecipeOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        return output.compareTo(((BaseAddOperation) o).output);
    }
}
