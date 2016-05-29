package org.winterblade.minecraft.harmony;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.IOperation;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.*;

/**
 * Created by Matt on 4/5/2016.
 */
public class OperationSet {
    private final static Set<BasePerPlayerOperation> activePerPlayerOperations = new HashSet<>();
    private final List<IOperation> operations = new ArrayList<>();
    private final String setName;
    private final boolean isBaseSet;
    private int duration;
    private boolean isInitialized = false;
    private int cooldown;
    private String[] removedSets;

    OperationSet(String setName) {
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
    static void addPerPlayerOperation(BasePerPlayerOperation operation) {
        activePerPlayerOperations.add(operation);
    }

    /**
     * Remove a per-player operation to run on the player when they connect
     * @param operation    The operation to remove
     */
    static void removePerPlayerOperation(BasePerPlayerOperation operation) {
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
     * Called from our internal scripts in order to create the operation.
     * @param operation A script object
     * @return          True if the operation processed fine; false otherwise.
     */
    public boolean addOperation(ScriptObjectMirror operation) {
        if(isInitialized) {
            LogHelper.error("Operations cannot be added after sets have been initialized.");
            return false;
        }

        if(!operation.containsKey("type")) {
            LogHelper.warn("All operation objects must contain a 'type' entry.");
            return false;
        }

        String type = operation.get("type").toString();

        BasicOperation inst = SetManager.createOperation(type, operation);
        if(inst == null) {
            LogHelper.warn("Unknown recipe operation type '" + type + "' for set '" + setName + "'.  Are you missing an addon?");
            return false;
        }

        return addOperation(inst);
    }

    /**
     * Gets the duration for the set
     * @return  The duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Called to set the duration of this operation
     * @param duration    The duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the cooldown for the set
     * @return  The cooldown
     */
    public int getCooldown() {
        return cooldown;
    }

    /**
     * Called to set the cooldown of this operation
     * @param cooldown    The cooldown to set
     */
    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Gets the sets to be removed when this one is applied
     * @return  The sets to remove:
     */
    public String[] getRemovedSets() {
        return removedSets != null ? removedSets : new String[0];
    }

    /**
     * Set the sets to remove when this one is applied
     * @param removedSets    The sets to remove
     */
    public void setRemovedSets(String[] removedSets) {
        this.removedSets = removedSets;
    }

    /**
     * Adds an operation to the set.
     * @param operation The operation.
     */
    boolean addOperation(IOperation operation) {
        // Make sure we're not trying to add something to a non-base set.
        if(operation.baseSetOnly() && !isBaseSet) {
            LogHelper.error("The operation '" + operation.toString() + "' can only be added to a base set; it cannot be added here.");
            return false;
        }

        return operations.add(operation);
    }

    /**
     * Initializes the operations
     */
    void init() {
        isInitialized = true;
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

        // Remove any operations now...
        if(0 < getRemovedSets().length) {
            CraftingHarmonicsMod.undoSets(getRemovedSets());
        }

        ProgressManager.pop(setProgress);
    }

    void undo() {
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
