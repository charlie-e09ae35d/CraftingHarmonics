package org.winterblade.minecraft.harmony.config.operations;

import net.minecraft.item.ItemStack;

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
        if(what == "*" || what == "*:*") return true;

        String name = recipeOutput.getUnlocalizedName();

        if(parts.length <= 1) {
            // Match the entire thing against the name
            return name.equals(what);
        } else if(parts[0] == "*") {
            // Match by item name:
            return name.equals(parts[1]);
        } else if(parts[1] == "*") {
            // TODO: Match by mod name
        } else {
            // TODO: Match by mod name
            return name.equals(parts[1]);
        }

        return false;
    }

    @Override
    public void Init() {
        parts = what.split(":");
    }
}
