package org.winterblade.minecraft.harmony;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.BaseRecipeOperation;
import org.winterblade.minecraft.harmony.api.ItemMissingException;

import java.util.HashSet;
import java.util.Set;

public abstract class BasePerPlayerOperation extends BaseRecipeOperation {
    private transient Set<String> appliedPlayers = new HashSet<>();

    /**
     * Called to initialize the set
     *
     * @throws ItemMissingException If something went wrong
     */
    @Override
    public final void Init() throws ItemMissingException {
        // Actually initialize the operation...
        doInit();

        // And now, make sure we have our applied players
        appliedPlayers.clear();
        appliedPlayers.addAll(CraftingHarmonicsMod.getSavedGameData().getAppliedPlayerIdsForOp(getId()));
    }

    public abstract void doInit() throws ItemMissingException;

    /**
     * Called to apply the set
     */
    @Override
    public final void Apply() {
        // Make sure we're on the server...
        if(FMLCommonHandler.instance().getMinecraftServerInstance() == null) return;

        // Register the operation
        CraftingSet.addPerPlayerOperation(this);

        // ... and run it on all connected players
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for(EntityPlayerMP player : playerList.getPlayerList()) {
            doApply(player);
        }
    }

    public final void Undo() {
        // Make sure we're on the server...
        if(FMLCommonHandler.instance().getMinecraftServerInstance() == null) return;

        // Remove the registration...
        CraftingSet.removePerPlayerOperation(this);

        // ... and undo it on everybody connected
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for(EntityPlayerMP player : playerList.getPlayerList()) {
            doUndo(player);
        }
    }

    public void doApply(EntityPlayerMP player) {
        // If we're doing once only, then pass on players that have it already...
        String playerId = player.getUniqueID().toString();
        if(onceOnly() && appliedPlayers.contains(playerId)) return;

        applyPerPlayer(player);
        if(onceOnly()) {
            appliedPlayers.add(playerId);
            CraftingHarmonicsMod.getSavedGameData().addPlayerForOperation(getId(), playerId);
        }
    }

    public abstract void applyPerPlayer(EntityPlayerMP player);

    public void doUndo(EntityPlayerMP player) {
        // If we're doing once only, or if the player doesn't have it...
        String playerId = player.getUniqueID().toString();
        if(onceOnly() || !appliedPlayers.contains(playerId)) return;

        undoPerPlayer(player);
        appliedPlayers.remove(playerId);
        CraftingHarmonicsMod.getSavedGameData().removePlayerForOperation(getId(), playerId);
    }

    public abstract void undoPerPlayer(EntityPlayerMP player);

    @Override
    public final boolean perPlayer() {
        return true;
    }

    /**
     * Called to check if the operation should be applied.
     *
     * @return True if the operation should execute now; false otherwise.
     */
    @Override
    public boolean shouldApply() {
        // We want to always apply; we'll handle onceOnly if it's necessary.
        return true;
    }
}
