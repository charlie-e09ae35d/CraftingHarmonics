package org.winterblade.minecraft.harmony.config.operations;

import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

/**
 * Created by Matt on 4/5/2016.
 */
public class RemoveOperation implements IConfigOperation {
    public String what;
    private String[] parts;

    public boolean Matches(ItemStack recipeOutput) {
        // If we have a null output... ignore it.
        if(recipeOutput == null) return false;

        // If we really, really want to remove everything...
        if(what.equals("*") || what.equals("*:*")) return true;

        String[] name = ItemRegistry.GetFullyQualifiedItemName(recipeOutput.getItem()).split(":");

        if(parts.length <= 1) {
            // Match the entire thing against the name
            return name[0].equals("minecraft") && name[1].equals(what);
        } else if(parts[0].equals("*")) {
            // Match by item name:
            return name[1].equals(parts[1]);
        } else if(parts[1].equals("*")) {
            // Match by mod name
            return name[0].equals(parts[0]);
        } else {
            // Match by item and mod name
            return name[0].equals(parts[0]) && name[1].equals(parts[1]);
        }
    }

    @Override
    public void Init() {
        parts = what.split(":");
    }
}
