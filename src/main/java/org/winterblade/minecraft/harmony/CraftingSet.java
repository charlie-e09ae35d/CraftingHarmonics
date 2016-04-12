package org.winterblade.minecraft.harmony;

import net.minecraftforge.fml.common.ProgressManager;
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
        ProgressManager.ProgressBar setProgress = ProgressManager.push("Initializing", operations.size());

        for(IRecipeOperation op : operations) {
            setProgress.step(op.toString());
            try {
                op.Init();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

        ProgressManager.pop(setProgress);

        // So we're only sorting it once...
        Collections.sort(operations);
    }

    void Apply() {
        ProgressManager.ProgressBar setProgress = ProgressManager.push("Applying", operations.size());

        for(IRecipeOperation op : operations) {
            setProgress.step(op.toString());
            try {
                op.Apply();
            }
            catch(Exception ex) {
                System.err.println("Error applying operation.\n" + ex.getMessage());
            }
        }

        ProgressManager.pop(setProgress);
    }
}
