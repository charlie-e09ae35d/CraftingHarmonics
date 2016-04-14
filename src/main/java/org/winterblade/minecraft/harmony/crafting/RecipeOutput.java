package org.winterblade.minecraft.harmony.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.winterblade.minecraft.harmony.api.IItemStackTransformer;

import java.util.*;

/**
 * Created by Matt on 4/14/2016.
 */
public class RecipeOutput {
    private static final String CH_DEFERRED_OUTPUT_TRANSFORM = "_CHDeferredOutputTransform";
    private static final Map<String, RecipeOutput> deferredTransforms = new HashMap<>();

    private ItemStack outputItem;
    private final List<IItemStackTransformer> transformerList = new ArrayList<>();
    private final String deferredId = UUID.randomUUID().toString();

    public RecipeOutput() {
        // When we get created, make sure we register our deferred ID:
        deferredTransforms.put(deferredId, this);
    }

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
     *
     * @param output    The output we're modifying
     * @param player    The player doing the crafting.
     */
    private void transformOutput(ItemStack output, EntityPlayer player) {
        for(IItemStackTransformer transformer : transformerList) {
            ItemStack transform = transformer.transform(output, player);
            if(transform == null) continue;

            // Copy the transformed info to this stack:
            output.setItem(transform.getItem());
            output.setItemDamage(transform.getItemDamage());
            output.setTagCompound(transform.getTagCompound());
            output.stackSize = transform.stackSize;
        }
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
        output.getTagCompound().setString(CH_DEFERRED_OUTPUT_TRANSFORM, deferredId);
        return output;
    }

    /**
     * Check if the given item stack needs to be handled by the deferred transform system.
     * @param itemStack The item stack to check
     * @return          True if it needs to be processed.
     */
    public static boolean isDeferredTransform(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(CH_DEFERRED_OUTPUT_TRANSFORM);
    }

    /**
     * Actually process the deferred transform
     * @param output        The output to process
     * @param player        The player doing the crafting
     * @param craftMatrix   The rest of the crafting inventory
     */
    public static void processDeferredTransform(ItemStack output, EntityPlayer player, IInventory craftMatrix) {
        // Get our ID and clean up:
        String deferredId = output.getTagCompound().getString(CH_DEFERRED_OUTPUT_TRANSFORM);
        output.getTagCompound().removeTag(CH_DEFERRED_OUTPUT_TRANSFORM);
        if(output.getTagCompound().hasNoTags()) output.setTagCompound(null);

        // Make sure we have something to do...
        if(!deferredTransforms.containsKey(deferredId)) return;

        // Process it..
        RecipeOutput recipeOutput = deferredTransforms.get(deferredId);
        recipeOutput.transformOutput(output, player);
    }
}
