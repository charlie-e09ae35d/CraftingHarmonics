package org.winterblade.minecraft.harmony.crafting.matchers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.Component;

/**
 * Created by Matt on 4/9/2016.
 */
@Component(properties = {"metadata"})
@PrioritizedObject(priority = Priority.HIGHEST)
public class MetadataMatcher implements IRecipeInputMatcher {
    private final int metadata;

    public MetadataMatcher(int metadata) {
        this.metadata = metadata;
    }

    /**
     * Determine if the input ItemStack matches our target.  Do not modify any of the parameters; it will
     * only end in pain.  Or awesome.  But likely pain.
     *
     * @param input     The input from the crafting grid.
     * @param inventory The inventory performing the craft.
     * @param output    The output item of the recipe
     * @return True if the given input matches the target.
     */
    @Override
    public boolean matches(ItemStack input, InventoryCrafting inventory,
                           ItemStack output) {
        return metadata == OreDictionary.WILDCARD_VALUE || metadata == input.getMetadata();
    }
}
