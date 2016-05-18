package org.winterblade.minecraft.harmony.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.winterblade.minecraft.harmony.CraftingSet;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class BasePerPlayerOperation extends BaseRecipeOperation {
    private transient Set<UUID> appliedPlayers = new HashSet<>();

    /**
     * Called to apply the set
     */
    @Override
    public final void Apply() {
        // Register the operation
        CraftingSet.addPerPlayerOperation(this);

        // ... and run it on all connected players
        PlayerList playerList = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList();

        for(EntityPlayerMP player : playerList.getPlayerList()) {
            doApply(player);
        }
    }

    public final void Undo() {
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
        if(onceOnly() && appliedPlayers.contains(player.getUniqueID())) return;

        applyPerPlayer(player);
        if(onceOnly()) {
            appliedPlayers.add(player.getUniqueID());
        }
    }

    public abstract void applyPerPlayer(EntityPlayerMP player);

    public void doUndo(EntityPlayerMP player) {
        // If we're doing once only, or if the player doesn't have it...
        if(onceOnly() || !appliedPlayers.contains(player.getUniqueID())) return;

        undoPerPlayer(player);
        appliedPlayers.remove(player.getUniqueID());
    }

    public abstract void undoPerPlayer(EntityPlayerMP player);

    @Override
    public final boolean perPlayer() {
        return true;
    }

    @Nullable
    @Override
    public final Side getSide() {
        return Side.SERVER;
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
