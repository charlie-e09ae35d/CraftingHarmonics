package org.winterblade.minecraft.harmony.integration.bloodmagic.operations;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.ritual.imperfect.IImperfectRitualStone;
import WayofTime.bloodmagic.api.ritual.imperfect.ImperfectRitual;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallbackContainer;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/28/2016.
 */
@Operation(name = "addImperfectRitual", dependsOn = "BloodMagic")
public class AddImperfectRitualOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private String name;
    private BlockMatcher block;
    private IEntityCallbackContainer[] onActivate;
    private int lpCost;
    private boolean lightShow;

    /*
     * Computed properties
     */
    private transient List<CallbackDrivenImperfectRitual> rituals = new ArrayList<>();

    /**
     * Should the operation be initialized/applied/undone/etc on the client
     *
     * @return True if it should, false otherwise.  Defaults to true.
     */
    @Override
    public boolean isClientOperation() {
        return false;
    }

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(block == null) throw new OperationException("Imperfect ritual must have a valid block to activate.");
        if(onActivate == null || onActivate.length <= 0) throw new OperationException("Imperfect ritual must have at least one onActivate callback.");
        rituals.clear();

        // Get all matching states...
        ImmutableList<IBlockState> validStates = block.getBlock().getBlockState().getValidStates();

        for(IBlockState state : validStates) {
            if(!block.matches(state)) continue;
            BlockStack blockStack = new BlockStack(block.getBlock(), block.getBlock().getMetaFromState(state));
            rituals.add(new CallbackDrivenImperfectRitual(name, blockStack, lpCost, lightShow,
                    "craftingharmonics.bloodmagic.imperfect_ritual."+block.getBlock().getUnlocalizedName(), onActivate));
        }
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {

    }

    public static class CallbackDrivenImperfectRitual extends ImperfectRitual {

        private final IEntityCallbackContainer[] callbacks;

        public CallbackDrivenImperfectRitual(String name, BlockStack requiredBlock, int activationCost, boolean lightshow,
                                             String unlocalizedName, IEntityCallbackContainer[] callbacks) {
            super(name, requiredBlock, activationCost, lightshow, unlocalizedName);
            this.callbacks = callbacks;
        }

        /**
         * Called when the player activates the ritual
         *
         * @param imperfectRitualStone - The {@link IImperfectRitualStone} that the ritual is bound to
         * @param player               - The player activating the ritual
         * @return - Whether activation was successful
         */
        @Override
        public boolean onActivate(IImperfectRitualStone imperfectRitualStone, EntityPlayer player) {
            for(IEntityCallbackContainer callback : callbacks) {
                callback.apply(player);
            }
            return true;
        }
    }
}
