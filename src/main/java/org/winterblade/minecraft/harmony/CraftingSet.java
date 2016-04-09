package org.winterblade.minecraft.harmony;

import org.winterblade.minecraft.harmony.api.IRecipeOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final List<IRecipeOperation> operations = new ArrayList<>();

    /**
     * Adds an operation to the set.
     * @param operation The operation.
     */
    public void AddOperation(IRecipeOperation operation) {
        operations.add(operation);
    }

    /**
     * Initializes the operations
     */
    void Init() {
        for(IRecipeOperation op : operations) {
            try {
                op.Init();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

        // So we're only sorting it once...
        Collections.sort(operations);
    }

    void Apply() {
        for(IRecipeOperation op : operations) {
            try {
                op.Apply();
            }
            catch(Exception ex) {
                System.err.println("Error applying operation.\n" + ex.getMessage());
            }
        }
    }
}
