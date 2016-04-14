package org.winterblade.minecraft.harmony.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Matt on 4/14/2016.
 */
public class RecipeOutput {
    private ItemStack outputItem;
    private final List<IItemStackTransformer> transformerList = new ArrayList<>();

    public void setOutputItem(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    /**
     * Adds a transformer to the transformer list.
     * @param transformer   The transformer to add.
     */
    public void addTransformer(IItemStackTransformer transformer) {
        transformerList.add(transformer);
    }

    /**
     * Apply any transformers and return the item stack.
     * @param player    The player doing the crafting.
     * @return          The transformed item stack
     */
    public ItemStack getTransformedOutput(EntityPlayer player) {
        ItemStack input = outputItem.copy();

        for(IItemStackTransformer transformer : transformerList) {
            input = transformer.transform(input, player);
            if(input == null) return null;
        }

        return input;
    }

    @Override
    public String toString() {
        return Item.itemRegistry.getNameForObject(outputItem.getItem()).toString();
    }

    public ItemStack getDeferredTransformOutput() {
        ItemStack output = outputItem.copy();

        if(!output.hasTagCompound()) {
            output.setTagCompound(new NBTTagCompound());
        }

        // Tag the item for later actioning in the crafting event:
        UUID deferredId = UUID.randomUUID();
        output.getTagCompound().setString("_CHDeferredOutputTransform", deferredId.toString());

        return output;
    }
}
