package org.winterblade.minecraft.harmony.crafting.integration.ticon.dto;

/**
 * Created by Matt on 5/3/2016.
 */
public class TinkersHeadMaterialStats {
    public int durability; // usually between 1 and 1000
    public int harvestLevel; // see HarvestLevels class
    public float attack; // usually between 0 and 10 (in 1/2 hearts, so divide by 2 for damage in hearts)
    public float miningSpeed; // usually between 1 and 10
}
