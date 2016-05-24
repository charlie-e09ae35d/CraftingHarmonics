package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

/**
 * Created by Matt on 5/16/2016.
 */
public class BaseScoreboardMatcher {
    private final String name;
    private final int min;
    private final int max;

    public BaseScoreboardMatcher(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    protected BaseMatchResult matches(World world, Entity entity) {
        if(world == null || entity == null) return BaseMatchResult.False;

        Scoreboard scoreboard = world.getScoreboard();
        if(scoreboard == null) return BaseMatchResult.False;

        ScoreObjective objective = scoreboard.getObjective(name);
        if(objective == null) return BaseMatchResult.False;

        int points = scoreboard.getOrCreateScore(entity.getName(),objective).getScorePoints();
        return min <= points && points <= max ? BaseMatchResult.True : BaseMatchResult.False;
    }
}
