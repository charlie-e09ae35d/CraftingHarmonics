package org.winterblade.minecraft.harmony.crafting.components;

import net.minecraft.item.ItemStack;

public class InputRecipeComponent extends BaseRecipeComponent {
    private String oreDict;
    private boolean fuzzyNbt;
    private boolean returnOnCraft;
    private ItemStack replace;

    public boolean isOreDict() {
        return oreDict != null;
    }
}
