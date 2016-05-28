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
    private final String name;
    private final String player;
    private final ScoreMatcherMode mode;
    private final int min;
    private final int max;

    public BaseScoreboardMatcher(String name, int min, int max, String player) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.player = player != null ? player : "";
        switch (this.player.toUpperCase()) {
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
                mode = this.player.equals("") ? ScoreMatcherMode.CURRENT : ScoreMatcherMode.SPECIFIC;
        }
    }

    protected BaseMatchResult matches(World world, Entity entity) {
        if(world == null || entity == null) return BaseMatchResult.False;

        Scoreboard scoreboard = world.getScoreboard();
        if(scoreboard == null) return BaseMatchResult.False;

        ScoreObjective objective = scoreboard.getObjective(name);
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

        int points = getPoints(scoreboard, objective, mode == ScoreMatcherMode.SPECIFIC ? player : entity.getName());
        return checkPoints(points) ? BaseMatchResult.True : BaseMatchResult.False;
    }

    /**
     * Checks to see if the points are between the given values
     * @param points    The points to check
     * @return          True if they're within the values, false otherwise
     */
    private boolean checkPoints(int points) {
        return min <= points && points <= max;
    }

    /**
     * Get the points for the given player
     * @param scoreboard    The scoreboard to check
     * @param objective     The objective to check
     * @param player        The player to check
     * @return              The number of points they have.
     */
    private int getPoints(Scoreboard scoreboard, ScoreObjective objective, String player) {
        // We do it this way to prevent creating the entry:
        Score score = scoreboard.getObjectivesForEntity(player).get(objective);
        return score != null ? score.getScorePoints() : 0;
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
        public String name;
        public int value;
        public String player;

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        public String getPlayer() {
            return player;
        }
    }
}
