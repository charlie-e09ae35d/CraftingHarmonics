package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/23/2016.
 */
public abstract class BaseStatMatcher {
    private final int min;
    private final int max;
    private StatBase stat;

    protected BaseStatMatcher(String statId, int min, int max) {
        this.min = min;
        this.max = max;

        stat = null;

        // Find our stat...
        for(StatBase it : StatList.ALL_STATS) {
            if(!it.statId.equals(statId)) continue;
            stat = it;
            break;
        }

        if(stat == null) {
            LogHelper.warn("Unable to find stat '" + statId + "'.");
        }
    }

    protected BaseMatchResult matches(Entity entity) {
        if(entity == null || !EntityPlayerMP.class.isAssignableFrom(entity.getClass()) || stat == null)
            return BaseMatchResult.False;

        int statValue = ((EntityPlayerMP) entity).getStatFile().readStat(stat);

        return min <= statValue && statValue <= max
                ? BaseMatchResult.True
                : BaseMatchResult.False;
    }
}
