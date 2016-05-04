package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.dto.TinkersExtraMaterialStats;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.dto.TinkersHandleMaterialStats;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.dto.TinkersHeadMaterialStats;
import org.winterblade.minecraft.harmony.utility.LogHelper;

/**
 * Created by Matt on 5/3/2016.
 */
@RecipeOperation(name = "setMaterialStats", dependsOn = "tconstruct")
public class SetMaterialStats extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private String material;

    private TinkersHeadMaterialStats head;
    private TinkersHandleMaterialStats handle;
    private TinkersExtraMaterialStats extra;

    /*
     * Computed properties
     */

    @Override
    public void Init() throws ItemMissingException {
        LogHelper.info("Test");
    }

    @Override
    public void Apply() {

    }

    @Override
    public void Undo() {

    }
}
