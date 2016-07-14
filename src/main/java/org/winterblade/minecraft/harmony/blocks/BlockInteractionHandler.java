package org.winterblade.minecraft.harmony.blocks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.blocks.IBlockDropMatcher;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.ItemUtility;
import org.winterblade.minecraft.harmony.entities.callbacks.BaseEntityCallback;
import org.winterblade.minecraft.harmony.mobs.MobTickRegistry;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.scripting.DeserializerHelpers;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

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
        this.matchers = matchers != null ? matchers : new IBlockDropMatcher[0];
        this.callbacks = callbacks != null ? callbacks : new IEntityCallback[0];
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

        // Run our callbacks, only for the main hand:
        // TODO: Consider moving this to the event handler to prevent going through everything else...
        if(evt.getHand() == EnumHand.MAIN_HAND) {
            MobTickRegistry.addCallbackSet(evt.getEntity(), callbacks, new BaseEntityMatcherData(evt.getEntity()));
        }

        return !cancelAfterMatch;
    }

    @ScriptObjectDeserializer(deserializes = BlockInteractionHandler.class)
    public static class Deserializer extends BaseMirroredDeserializer {
        private static final IScriptObjectDeserializer CALLBACK_DESERIALIZER = new BaseEntityCallback.Deserializer();

        @Override
        protected Object DeserializeMirror(ScriptObjectMirror mirror) {
            // Gather callbacks:
            IEntityCallback[] callbacks = DeserializerHelpers.convertArrayWithDeserializer(mirror, "onUsed",
                    CALLBACK_DESERIALIZER, IEntityCallback.class);

            // And matchers:
            ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{IBlockDropMatcher.class}, mirror);
            List<IBlockDropMatcher> components = registry.getComponentsOf(IBlockDropMatcher.class);

            boolean cancel = mirror.containsKey("cancelAfterMatch")
                    && (Boolean) ScriptUtils.convert(mirror.get("cancelAfterMatch"), Boolean.class);

            return new BlockInteractionHandler(components != null ? components.toArray(new IBlockDropMatcher[components.size()]) : null,
                    callbacks, cancel);
        }
    }
}
