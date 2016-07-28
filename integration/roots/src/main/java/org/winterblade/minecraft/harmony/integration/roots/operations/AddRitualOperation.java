package org.winterblade.minecraft.harmony.integration.roots.operations;

import elucent.roots.ritual.RitualBase;
import elucent.roots.ritual.rituals.RitualCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.common.utility.ColorHelper;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.roots.RootsRegistry;

import java.awt.*;

/**
 * Created by Matt on 7/27/2016.
 */
@Operation(name = "Roots.addRitual", dependsOn = "roots")
public class AddRitualOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private ItemStack output;
    private ItemStack[] incense;
    private ItemStack[] input;
    private StandingStonePosition[] stones;
    private String name;
    private String color;
    private String color2;

    /*
     * Computed properties
     */
    private transient RitualBase ritual;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        // Set up the colors:
        Color c = ColorHelper.convertHex(color);
        Color c2 = ColorHelper.convertHex((color2 != null) ? color2 : color);

        // Actually set up the ritual:
        ritual = new RitualCrafting(name, c.getRed(), c.getGreen(), c.getBlue(),
                    c2.getRed(), c2.getGreen(), c2.getBlue())
                .setResult(output);

        // Add any incenses
        if(incense != null) {
            for (ItemStack stack : incense) {
                ritual.addIncense(stack);
            }
        }

        // Add the inputs:
        if(input != null) {
            for (ItemStack stack : input) {
                ritual.addIngredient(stack);
            }
        }

        // Add in standing stones...
        if(stones != null) {
            for (StandingStonePosition stone : stones) {
                Block b = RootsRegistry.getStandingStone(stone.tier);
                ritual.addBlock(b, stone.x, stone.y, stone.z);
            }
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Adding Roots ritual for '{}'.", ItemUtility.outputItemName(output));
        RootsRegistry.addRitual(ritual);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        RootsRegistry.removeRitual(ritual);
    }

    public static class StandingStonePosition {
        private int tier;
        private int x;
        private int y;
        private int z;
    }
}
