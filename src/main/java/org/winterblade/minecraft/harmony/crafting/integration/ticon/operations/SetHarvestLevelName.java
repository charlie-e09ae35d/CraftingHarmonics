package org.winterblade.minecraft.harmony.crafting.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.utility.LogHelper;

/**
 * Created by Matt on 5/4/2016.
 */
@RecipeOperation(name = "setHarvestLevelName", dependsOn = "tconstruct")
public class SetHarvestLevelName extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private int level;
    private String name;
    private String color;

    /*
     * Computed properties
     */
    private transient String newLevel;
    private transient String oldLevel;

    @Override
    public void Init() throws ItemMissingException {
        if(level < 0) throw new ItemMissingException("Harvest level to set cannot be less than zero.");
        oldLevel = ReflectedTinkerRegistry.getHarvestLevelName(level);
        newLevel = ReflectedTinkerRegistry.encodeColor(color) + name;
    }

    @Override
    public void Apply() {
        LogHelper.info("Updating harvest level #" + level + " to be called " + name);
        ReflectedTinkerRegistry.setHarvestLevelName(level, newLevel);
    }

    @Override
    public void Undo() {
        ReflectedTinkerRegistry.setHarvestLevelName(level, oldLevel);
    }
}
