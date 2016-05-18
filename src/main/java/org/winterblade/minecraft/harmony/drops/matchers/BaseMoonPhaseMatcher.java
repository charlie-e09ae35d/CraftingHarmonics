package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

/**
 * Created by Matt on 5/15/2016.
 */
public abstract class BaseMoonPhaseMatcher {
    private final int phase;

    public BaseMoonPhaseMatcher(MoonPhase phase) {
        this.phase = phase.getPhase();
    }

    protected BaseDropMatchResult matches(World world) {
        return phase == world.provider.getMoonPhase(world.getWorldInfo().getWorldTime())
                ? BaseDropMatchResult.True
                : BaseDropMatchResult.False;
    }

    public enum MoonPhase {
        Full(0),
        WaningGibbous(1),
        ThirdQuarter(2),
        WaningCrescent(3),
        New(4),
        WaxingCrescent(5),
        FirstQuarter(6),
        WaxingGibbous(7);

        private final int phase;

        MoonPhase(int phase) {
            this.phase = phase;
        }

        public int getPhase() {
            return phase;
        }
    }
}
