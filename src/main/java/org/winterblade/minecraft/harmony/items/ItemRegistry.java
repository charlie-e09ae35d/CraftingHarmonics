package org.winterblade.minecraft.harmony.items;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/22/2016.
 */
public class ItemRegistry {
    private ItemRegistry() {}

    public static final ItemRegistry instance = new ItemRegistry();

    private final List<ItemBlockData> preventedItems = new ArrayList<>();

    /**
     * Called to determine if an item should be allowed to be used
     * @param evt    The event to check
     * @return       True if it should be cancelled, false otherwise.
     */
    public boolean shouldCancelUse(PlayerInteractEvent evt) {
        for (ItemBlockData preventedItem : preventedItems) {
            if(preventedItem.matches(evt)) return true;
        }
        return false;
    }


    /**
     * Adds the given input to the list of items to prevent using.
     * @param input    The input to prevent.
     * @param onBlock  The callbacks to run when it's been blocked
     * @param matchers The matchers to check to do the blocking.
     */
    public ItemBlockData preventUse(@Nonnull RecipeInput[] input, IEntityCallback[] onBlock, List<IEntityMatcher> matchers) {
        ItemBlockData data = new ItemBlockData(input, onBlock, matchers);
        preventedItems.add(data);
        return data;
    }

    /**
     * Removes the given input from the list of items to prevent using
     * @param input    The input to allow again.
     */
    public void allowUse(@Nonnull ItemBlockData input) {
        preventedItems.remove(input);
    }

    public static class ItemBlockData {
        private final RecipeInput[] inputs;
        private final IEntityCallback[] onBlock;
        private final List<IEntityMatcher> matchers;

        private ItemBlockData(RecipeInput[] inputs, IEntityCallback[] onBlock, List<IEntityMatcher> matchers) {
            this.inputs = inputs;
            this.onBlock = onBlock;
            this.matchers = matchers;
        }

        private boolean matches(PlayerInteractEvent evt) {
            // Quickly check our inputs...
            boolean matches = false;
            for (RecipeInput input : inputs) {
                if(!ItemUtility.recipeInputsMatch(input.getFacsimileItem(), evt.getItemStack())) continue;
                matches = true;
                break;
            }

            if(!matches) return false;

            List<Runnable> runnables = new ArrayList<>();
            BaseEntityMatcherData matcherData = new BaseEntityMatcherData(evt.getEntity());

            // Now check our matchers...
            for(IEntityMatcher matcher : matchers) {
                BaseMatchResult res = matcher.isMatch(evt.getEntity(), matcherData);
                if(!res.isMatch()) return false;
                if(res.getCallback() != null) runnables.add(res.getCallback());
            }

            // Fire the runnables from our matchers...
            for (Runnable runnable : runnables) {
                runnable.run();
            }

            // Also fire our callbacks...
            MobTickRegistry.addCallbackSet(evt.getEntity(), onBlock, matcherData);

            return true;
        }
    }
}
