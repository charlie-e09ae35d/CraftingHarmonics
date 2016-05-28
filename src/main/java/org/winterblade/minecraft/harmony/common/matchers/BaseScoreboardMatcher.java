package org.winterblade.minecraft.harmony.common.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;

import java.util.Collection;

/**
 * Created by Matt on 5/16/2016.
 */
public class BaseScoreboardMatcher {
    private final ScoreboardMatchData data;
    private final ScoreMatcherMode mode;

    public BaseScoreboardMatcher(ScoreboardMatchData data) {
        this.data = data;
        switch (data.getPlayer().toUpperCase()) {
            case "ALL":
                mode = ScoreMatcherMode.ALL;
                break;
            case "ALLONLINE":
                mode = ScoreMatcherMode.ALLONLINE;
                break;
            case "ANY":
                mode = ScoreMatcherMode.ANY;
                break;
            case "ANYONLINE":
                mode = ScoreMatcherMode.ANYONLINE;
                break;
            default:
                mode = data.getPlayer().equals("") ? ScoreMatcherMode.CURRENT : ScoreMatcherMode.SPECIFIC;
        }
    }

    protected BaseMatchResult matches(World world, Entity entity) {
        if(world == null || entity == null) return BaseMatchResult.False;

        Scoreboard scoreboard = world.getScoreboard();
        if(scoreboard == null) return BaseMatchResult.False;

        ScoreObjective objective = scoreboard.getObjective(data.getName());
        if(objective == null) return BaseMatchResult.False;

        // If we have a special mode:
        switch (mode) {
            case ALL:
                return checkAllForScoreboard(scoreboard, objective, false);
            case ALLONLINE:
                return checkOnlineForScoreboard(scoreboard, objective, false);
            case ANY:
                return checkAllForScoreboard(scoreboard, objective, true);
            case ANYONLINE:
                return checkOnlineForScoreboard(scoreboard, objective, true);
        }

        int points = getPoints(scoreboard, objective, mode == ScoreMatcherMode.SPECIFIC ? data.getPlayer() : entity.getName());
        return checkPoints(points) ? BaseMatchResult.True : BaseMatchResult.False;
    }

    /**
     * Checks to see if the points are between the given values
     * @param points    The points to check
     * @return          True if they're within the values, false otherwise
     */
    private boolean checkPoints(int points) {
        return data.getMin() <= points && points <= data.getMax();
    }

    /**
     * Get the points for the given player
     * @param scoreboard    The scoreboard to check
     * @param objective     The objective to check
     * @param player        The player to check
     * @return              The number of points they have.
     */
    private int getPoints(Scoreboard scoreboard, ScoreObjective objective, String player) {
        Score score = scoreboard.getObjectivesForEntity(player).get(objective);

        // If we found it...
        if(score != null) return score.getScorePoints();

        // If we need to create it...
        if(data.isCreate()) {
            scoreboard.getOrCreateScore(player, objective).setScorePoints(data.getDefaultValue());
        }

        // Return the default value, since we couldn't find it...
        return data.getDefaultValue();
    }

    /**
     * Checks all entities ever for the given scoreboard
     * @param scoreboard    The scoreboard to check
     * @param objective     The objective to check for
     * @param any           If we're looking for any entity
     * @return              The result of the search.
     */
    private BaseMatchResult checkAllForScoreboard(Scoreboard scoreboard, ScoreObjective objective, boolean any) {
        Collection<Score> sortedScores = scoreboard.getSortedScores(objective);
        for(Score score : sortedScores) {
            boolean matches = checkPoints(score.getScorePoints());

            if(matches && any) return BaseMatchResult.True;
            else if(!matches && !any) return BaseMatchResult.False;
        }

        return BaseMatchResult.True;
    }

    /**
     * Checks online players for the given scoreboard
     * @param scoreboard    The scoreboard to check
     * @param objective     The objective to check for
     * @param any           If we're looking for any entity
     * @return              The result of the search.
     */
    private BaseMatchResult checkOnlineForScoreboard(Scoreboard scoreboard, ScoreObjective objective, boolean any) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if(server == null) return BaseMatchResult.False; // This shouldn't be called on the client, but, in case it was...

        for(EntityPlayerMP player : server.getPlayerList().getPlayerList()) {
            boolean matches = checkPoints(getPoints(scoreboard, objective, player.getName()));

            if(matches && any) return BaseMatchResult.True;
            else if(!matches && !any) return BaseMatchResult.False;
        }

        return BaseMatchResult.True;
    }

    private enum ScoreMatcherMode {
        CURRENT,
        SPECIFIC,
        ALL,
        ALLONLINE,
        ANY,
        ANYONLINE
    }

    public static class ScoreboardMatchData {
        private String name = "";
        private int value = 0;
        private int min = Integer.MIN_VALUE;
        private int max = Integer.MAX_VALUE;
        private String player = "";
        private boolean create = false;
        private int defaultValue = 0;

        public String getName() {
            return this.name;
        }

        public String getPlayer() {
            return this.player;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public ScoreboardMatchData withMinValue() {
            if(min == Integer.MIN_VALUE) min = value;
            return this;
        }

        public ScoreboardMatchData withMaxValue() {
            if(max == Integer.MAX_VALUE) max = value;
            return this;
        }

        public boolean isCreate() {
            return create;
        }

        public int getDefaultValue() {
            return defaultValue;
        }
    }
}
