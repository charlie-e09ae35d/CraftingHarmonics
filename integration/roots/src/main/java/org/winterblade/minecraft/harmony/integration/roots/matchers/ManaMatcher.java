package org.winterblade.minecraft.harmony.integration.roots.matchers;

import elucent.roots.capability.mana.ManaProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 7/30/2016.
 */
public abstract class ManaMatcher {
    private final int min;
    private final int max;

    public ManaMatcher(ManaData data) {
        min = data.min;
        max = data.max;
    }

    public BaseMatchResult matches(Entity entity) {
        if(!EntityPlayer.class.isAssignableFrom(entity.getClass())
                || !entity.hasCapability(ManaProvider.manaCapability, null)) return BaseMatchResult.False;

        float mana = ManaProvider.get((EntityPlayer) entity).getMana();

        return (min <= mana && mana <= max)
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }

    public static class ManaData {
        private int min;
        private int max;
    }


}
