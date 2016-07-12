package org.winterblade.minecraft.harmony.blocks;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 7/11/2016.
 */
public class BlockInteractionHandler {
    private final static ItemStack dummy = ItemUtility.getDummyItemStack();

    private final IBlockDropMatcher[] matchers;
    private final IEntityCallback[] callbacks;
    private final boolean cancelAfterMatch;

    public BlockInteractionHandler(IBlockDropMatcher[] matchers, IEntityCallback[] callbacks, boolean cancelAfterMatch) {
        this.matchers = matchers;
        this.callbacks = callbacks;
        this.cancelAfterMatch = cancelAfterMatch;
    }

    /**
     * Handle the event, returning if processing should continue
     *
     * @param evt          The event to handle
     * @param simulatedEvt A simulated drop event to pass on to the matchers
     * @return True if processing should continue; false to cancel the underlying event.
     */
    public boolean handle(PlayerInteractEvent.RightClickBlock evt, BlockEvent.HarvestDropsEvent simulatedEvt) {
        List<Runnable> matcherCallbacks = new ArrayList<>();

        // Check our matchers:
        for (IBlockDropMatcher matcher : matchers) {
            BaseMatchResult result = matcher.isMatch(simulatedEvt, dummy);
            if (!result.isMatch()) return true;
            if (result.getCallback() != null) matcherCallbacks.add(result.getCallback());
        }

        // Run our matcher callbacks:
        for (Runnable matcherCallback : matcherCallbacks) {
            matcherCallback.run();
        }

        // Run our callbacks, if we're on the server:
        MobTickRegistry.addCallbackSet(evt.getEntity(), callbacks, new BaseEntityMatcherData(evt.getEntity()));

        return !cancelAfterMatch;
    }
}
