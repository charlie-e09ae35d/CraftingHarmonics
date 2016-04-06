package org.winterblade.minecraft.harmony;

import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final List<IRecipeOperation> operations = new ArrayList<IRecipeOperation>();

    /**
     * Creates a crafting set using the given set of operations
     * @param configOperations
     */
    public CraftingSet(IRecipeOperation[] configOperations) {
        Collections.addAll(operations, configOperations);
    }

    /**
     * Initializes the operations
     */
    public void Init() {
        for(IRecipeOperation op : operations) {
            try {
                op.Init();
            } catch (ItemMissingException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public void Apply() {
        for(IRecipeOperation op : operations) {
            try {
                op.Apply();
            }
            catch(Exception ex) {
                System.err.println("Error applying operation.\n" + ex.getStackTrace());
            }
        }
    }
}
