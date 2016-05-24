package org.winterblade.minecraft.harmony;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.*;

/**
 * Created by Matt on 4/5/2016.
 */
public class CraftingSet {
    private final static Set<BasePerPlayerOperation> activePerPlayerOperations = new HashSet<>();
    private final List<IOperation> operations = new ArrayList<>();
    private final String setName;
    private final boolean isBaseSet;

    public CraftingSet(String setName) {
        this.setName = setName;
        switch (setName) {
            case "default":
            case "peaceful":
            case "normal":
            case "hard":
                isBaseSet = true;
                break;
            default:
                isBaseSet = false;
                break;
        }
    }

    /**
     * Add a per-player operation to run on the player when they connect
     * @param operation    The operation to add
     */
    public static void addPerPlayerOperation(BasePerPlayerOperation operation) {
        activePerPlayerOperations.add(operation);
    }

    /**
     * Remove a per-player operation to run on the player when they connect
     * @param operation    The operation to remove
     */
    public static void removePerPlayerOperation(BasePerPlayerOperation operation) {
        activePerPlayerOperations.remove(operation);
    }

    /**
     * Called in order to run all active per-player operations
     * @param player    The player to run the command on.
     */
    public static void runPerPlayerOperations(EntityPlayerMP player) {
        for(BasePerPlayerOperation op : activePerPlayerOperations) {
            try {
                op.doApply(player);
            } catch(Exception ex) {
                LogHelper.error("Error applying per-player operation '" + op.toString() + "' on player: " + player.getName(), ex);
            }
        }
    }

    /**
     * Adds an operation to the set.
     * @param operation The operation.
     */
    public void addOperation(IOperation operation) {
        // Make sure we're not trying to add something to a non-base set.
        if(operation.baseSetOnly() && !isBaseSet) {
            LogHelper.error("The operation '" + operation.toString() + "' can only be added to a base set; it cannot be added here.");
            return;
        }
        operations.add(operation);
    }

    /**
     * Initializes the operations
     */
    void init() {
        ProgressManager.ProgressBar setProgress = ProgressManager.push("Initializing", operations.size());
        Side curSide = FMLCommonHandler.instance().getEffectiveSide();

        for(IOperation op : operations) {
            setProgress.step(op.toString());

            // If we're not supposed to run here...
            if((curSide == Side.CLIENT && !op.isClientOperation())
                    || (curSide == Side.SERVER && !op.isServerOperation())) continue;

            try {
                op.runInit();
            } catch (Exception ex) {
                LogHelper.error("Error initializing operation.", ex);
            }
        }

        ProgressManager.pop(setProgress);

        // So we're only sorting it once...
        Collections.sort(operations);
    }

    void apply() {
        ProgressManager.ProgressBar setProgress = ProgressManager.push("Applying", operations.size());
        Side curSide = FMLCommonHandler.instance().getEffectiveSide();

        for(IOperation op : operations) {
            setProgress.step(op.toString());

            // If we're not supposed to run here...
            if((curSide == Side.CLIENT && !op.isClientOperation())
                    || (curSide == Side.SERVER && !op.isServerOperation())
                    || !op.shouldApply()) continue;

            try {
                op.runApply();
            }
            catch(Exception ex) {
                LogHelper.error("Error applying operation.", ex);
            }
        }

        ProgressManager.pop(setProgress);
    }

    public void undo() {
        // Reverse the sort order... badly.
        List<IOperation> revserseOps = new ArrayList<>(operations);
        Collections.reverse(revserseOps);
        Side curSide = FMLCommonHandler.instance().getEffectiveSide();

        // Undo the operations in the opposite way we applied them:
        for(IOperation op : revserseOps) {
            // If we're not supposed to run here...
            if((curSide == Side.CLIENT && !op.isClientOperation())
                    || (curSide == Side.SERVER && !op.isServerOperation())
                    || !op.shouldUndo()) continue;

            try {
                op.runUndo();
            }
            catch(Exception ex) {
                LogHelper.error("Error undoing operation.", ex);
            }
        }
    }
}
