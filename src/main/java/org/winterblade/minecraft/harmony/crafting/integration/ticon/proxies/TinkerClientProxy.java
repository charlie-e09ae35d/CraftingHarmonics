package org.winterblade.minecraft.harmony.crafting.integration.ticon.proxies;

import slimeknights.tconstruct.library.materials.Material;

public class TinkerClientProxy extends TinkerCommonProxy {
    /**
     * Updates the render info on a given material
     * @param material    The source material
     * @param output      The output material
     */
    public void updateRenderInfo(Material material, Material output) {
        output.setRenderInfo(material.renderInfo);
    }
}
