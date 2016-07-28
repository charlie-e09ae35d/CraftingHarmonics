package org.winterblade.minecraft.harmony.integration.roots;

import elucent.roots.RegistryManager;
import elucent.roots.ritual.RitualBase;
import elucent.roots.ritual.RitualManager;
import net.minecraft.block.Block;

/**
 * Created by Matt on 7/27/2016.
 */
public class RootsRegistry {
    private RootsRegistry() {}

    public static void addRitual(RitualBase ritual) {
        RitualManager.rituals.add(ritual);
    }

    public static void removeRitual(RitualBase ritual) {
        RitualManager.rituals.remove(ritual);
    }

    /**
     * Get the standing stone for a given tier
     * @param tier    The tier to get
     * @return        The standing stone's block
     */
    public static Block getStandingStone(int tier) {
        switch (tier) {
            case 1:
            default:
                return RegistryManager.standingStoneT1;
            case 2:
                return RegistryManager.standingStoneT2;
        }
    }
}
