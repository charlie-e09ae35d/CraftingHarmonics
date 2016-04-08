package org.winterblade.minecraft.harmony;

import org.winterblade.minecraft.harmony.api.IRecipeOperation;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;

import java.util.*;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final Collection<IRecipeOperation> operations = new PriorityQueue<>();

    /**
     * Creates a crafting set using the given set of operations
     * @param recipeOperations  The operations to add to this set.
     */
    public CraftingSet(IRecipeOperation[] recipeOperations) {
        try {
            Collections.addAll(operations, recipeOperations);
        }
        catch(Exception ex) {
            // TODO: Better logging here.
            System.err.println("Error processing operations in set.");
        }
    }

    /**
     * Initializes the operations
     */
    void Init() {
        for(IRecipeOperation op : operations) {
            try {
                op.Init();
            } catch (ItemMissingException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    void Apply() {
        for(IRecipeOperation op : operations) {
            try {
                op.Apply();
            }
            catch(Exception ex) {
                System.err.println("Error applying operation.\n" + Arrays.toString(ex.getStackTrace()));
            }
        }
    }
}
