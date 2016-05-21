package org.winterblade.minecraft.harmony.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/4/2016.
 */
@Operation(name = "setHarvestLevelName", dependsOn = "tconstruct")
public class SetHarvestLevelName extends BasicOperation {
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
    public void init() throws OperationException {
        if(level < 0) throw new OperationException("Harvest level to set cannot be less than zero.");
        oldLevel = ReflectedTinkerRegistry.getHarvestLevelName(level);
        newLevel = ReflectedTinkerRegistry.encodeColor(color) + name;
    }

    @Override
    public void apply() {
        LogHelper.info("Updating harvest level #" + level + " to be called " + name);
        ReflectedTinkerRegistry.setHarvestLevelName(level, newLevel);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.setHarvestLevelName(level, oldLevel);
    }
}
