package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.bloodmagic.ReflectedBloodMagicRegistry;

import java.util.Arrays;

/**
 * Created by Matt on 4/21/2016.
 */
@Operation(name = "addBloodAltarRecipe", dependsOn = "BloodMagic")
public class AddAltarRecipe extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private ItemStack[] with;
    private int minTier;
    private int lpCost;
    private int consumeRate;
    private int drainRate;

    /*
     * Computed properties
     */
    AltarRecipeRegistry.AltarRecipe recipe;

    @Override
    public void init() throws OperationException {
        recipe = new AltarRecipeRegistry.AltarRecipe(Arrays.asList(with), output, EnumAltarTier.values()[minTier-1], lpCost, consumeRate, drainRate, false);
    }

    @Override
    public void apply() {
        LogHelper.info("Adding blood altar recipe for '" + ItemUtility.outputItemName(output) + "'.");
        ReflectedBloodMagicRegistry.addAltarRecipe(recipe);
    }

    @Override
    public void undo() {
        ReflectedBloodMagicRegistry.removeAltarRecipe(recipe);
    }
}
