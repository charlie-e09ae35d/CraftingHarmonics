package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
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
    protected String nbt;

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

        if(nbt != null && !nbt.equals("")) {
            try {
                outputItemStack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
            } catch (NBTException e) {
                // Yeah, we're just passing it on.
                throw new RuntimeException("Unable to convert input NBT into something readable by Minecraft; got response '" + e.getMessage() + "'.");
            }
        }

        // Set the display name afterwards, because setting NBT would overwrite it...
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
        BaseAddOperation other = (BaseAddOperation)o;
        if(output == null) return 1;
        if(other.output == null) return -1;
        return output.compareTo(other.output);
    }
}
