package org.winterblade.minecraft.harmony.crafting.operations;

import net.minecraft.item.Item;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.RecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;

/**
 * Created by Matt on 5/2/2016.
 */
@RecipeOperation(name = "setToolHarvestLevel")
public class SetToolLevelOperation extends BaseRecipeOperation {
    /*
     * Serialized properties
     */
    private Item what;
    private int to;
    private String as;

    /*
     * Computed properties
     */
    private transient int from;

    @Override
    public void Init() throws ItemMissingException {
        if(what == null) throw new ItemMissingException("Cannot set tool harvest level on unknown item.");

        from = what.getHarvestLevel(null, as);
    }

    @Override
    public void Apply() {
        what.setHarvestLevel(as, to);
    }

    @Override
    public void Undo() {
        what.setHarvestLevel(as, from);
    }
}
