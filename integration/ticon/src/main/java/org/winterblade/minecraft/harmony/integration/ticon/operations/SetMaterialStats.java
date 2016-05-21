package org.winterblade.minecraft.harmony.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import org.winterblade.minecraft.harmony.integration.ticon.dto.TinkersExtraMaterialStats;
import org.winterblade.minecraft.harmony.integration.ticon.dto.TinkersHandleMaterialStats;
import org.winterblade.minecraft.harmony.integration.ticon.dto.TinkersHeadMaterialStats;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

/**
 * Created by Matt on 5/3/2016.
 */
@Operation(name = "setMaterialStats", dependsOn = "tconstruct")
public class SetMaterialStats extends BasicOperation {
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
    private transient Material original;
    private transient Material updated;

    @Override
    public void init() throws OperationException {
        // Get our original and copy:
        original = ReflectedTinkerRegistry.getMaterial(material);
        if(original == null) throw new OperationException("Unable to get TiCon material '" + material + "'.");

        updated = ReflectedTinkerRegistry.copyMaterial(original);

        // Figure out what to change:
        if(head != null) {
            updated.addStats(new HeadMaterialStats(head.durability, head.miningSpeed, head.attack, head.harvestLevel));
        }

        if(handle != null) {
            updated.addStats(new HandleMaterialStats(handle.modifier, handle.durability));
        }

        if(extra != null) {
            updated.addStats(new ExtraMaterialStats(extra.bonusDurability));
        }
    }

    @Override
    public void apply() {
        LogHelper.info("Updating stats for material '" + material + "'");
        ReflectedTinkerRegistry.setMaterial(updated);
    }

    @Override
    public void undo() {
        ReflectedTinkerRegistry.setMaterial(original);
    }
}
