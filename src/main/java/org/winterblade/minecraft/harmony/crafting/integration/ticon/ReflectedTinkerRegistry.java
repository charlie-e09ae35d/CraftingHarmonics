package org.winterblade.minecraft.harmony.crafting.integration.ticon;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;

import java.util.List;

/**
 * Created by Matt on 4/21/2016.
 */
public class ReflectedTinkerRegistry {
    private static List<AlloyRecipe> alloyRegistry;

    static {
        // Hook directly into this because we can't remove an alloy after it's been added:
        alloyRegistry = ObfuscationReflectionHelper.getPrivateValue(TinkerRegistry.class, null, "alloyRegistry");
    }

    private ReflectedTinkerRegistry() {}

    public static void addAlloy(AlloyRecipe recipe) {
        alloyRegistry.add(recipe);
    }

    public static void removeAlloy(AlloyRecipe recipe) {
        alloyRegistry.remove(recipe);
    }
}
