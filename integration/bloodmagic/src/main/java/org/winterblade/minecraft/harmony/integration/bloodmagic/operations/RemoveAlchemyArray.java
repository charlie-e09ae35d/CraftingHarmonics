package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 4/22/2016.
 */
@RecipeOperation(name = "removeAlchemyArray", dependsOn = "BloodMagic")
public class RemoveAlchemyArray extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private ItemStack catalyst;
    private RecipeInput with;

    /*
     * Computed properties
     */
    private transient Object input;
    private ReflectedBloodMagicRegistry.RemovedAlchemyArray recipe;

    @Override
    public void Init() throws ItemMissingException {
        input = with.getFacsimileItem();
    }

    @Override
    public void Apply() {
        // If we have no identifiable property:
        if(with == null) return;

        if(input instanceof String) {
            LogHelper.info("Removing Alchemy Array for '" + input.toString() + "'.");
            recipe = ReflectedBloodMagicRegistry.removeAlchemyArray(input.toString(), catalyst);
        } else if(input instanceof ItemStack) {
            LogHelper.info("Removing Alchemy Array for '" + ItemUtility.outputItemName((ItemStack) input) + "'.");
            recipe = ReflectedBloodMagicRegistry.removeAlchemyArray((ItemStack)input, catalyst);
        }
    }

    @Override
    public void Undo() {
        if(recipe == null) return;
        ReflectedBloodMagicRegistry.addAlchemyArray(recipe.input, recipe.catalyst, recipe.effect, recipe.renderer);
    }
}
