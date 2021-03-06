package org.winterblade.minecraft.harmony.common.crafting.operations;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.api.crafting.components.RecipeComponent;

/**
 * Created by Matt on 4/6/2016.
 */
public abstract class BaseAddOperation extends BasicOperation {
    /**
     * Serialized properties:
     */
    protected RecipeComponent output;
    protected int quantity;
    protected String displayName;
    protected NBTTagCompound nbt;

    @Override
    public void init() throws OperationException
    {
        if (output.getItemStack() == null)
            throw new OperationException("Unable to find requested output item " + output.toString());

        ItemUtility.updateStackQuantity(output.getItemStack(), quantity);

        if(nbt != null && !nbt.hasNoTags()) {
            output.getItemStack().setTagCompound(nbt);
        }

        // Set the display name afterwards, because setting NBT would overwrite it...
        if(displayName != null && !displayName.equals("")) {
            output.getItemStack().setStackDisplayName(displayName);
        }
    }


    @Override
    public int compareTo(IOperation o) {
        int baseCompare = super.compareTo(o);
        if(baseCompare != 0) return baseCompare;

        // Keep classes together:
        if(!(o.getClass().equals(getClass())))
            return o.getClass().getSimpleName().compareTo(getClass().getSimpleName());

        // Otherwise, sort on name:
        BaseAddOperation other = (BaseAddOperation)o;
        if(output == null) return 1;
        if(other.output == null) return -1;
        // More sanity checks on this...
        if(output.getItemStack() == null) return 1;
        if(other.output.getItemStack() == null) return -1;
        String thisUnlocName = output.getItemStack().getUnlocalizedName();
        String otherUnlocName = other.output.getItemStack().getUnlocalizedName();
        if(thisUnlocName == null) return 1;
        if(otherUnlocName == null) return -1;
        return thisUnlocName.compareTo(
                other.output.getItemStack().getUnlocalizedName());
    }

    /**
     * Quick and dirty representation of a recipe wrapper
     * @param recipe        The recipe to (potentially) wrap
     * @param components    The components to check
     * @return              The wrapped recipe; or the original if it didn't need wrapped.
     */
    protected IRecipe Wrap(IRecipe recipe, RecipeComponent[] components) {
        return recipe;
    }
}
