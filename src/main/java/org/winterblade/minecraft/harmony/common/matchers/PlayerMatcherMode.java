package org.winterblade.minecraft.harmony.common.matchers;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/30/2016.
 */
public enum PlayerMatcherMode {
    CURRENT,
    SPECIFIC,
    ALL,
    ALLONLINE,
    ANY,
    ANYONLINE;

    public static PlayerMatcherMode convert(String player) {
        if(player == null || player.equals("")) return CURRENT;
        switch (player.toUpperCase()) {
            case "ALL":
                return ALL;
            case "ALLONLINE":
                return ALLONLINE;
            case "ANY":
                return ANY;
            case "ANYONLINE":
                return ANYONLINE;
            default:
                return SPECIFIC;
        }
    }

    /**
     * Returns the proper type, checked against a player name
     * @param name    The name, if any, to check against
     * @return        The current mode, if it isn't SPECIFIC, CURRENT if the name is null/empty, or SPECIFIC otherwise.
     */
    public PlayerMatcherMode checkAgainstPlayer(@Nullable String name) {
        return this != SPECIFIC
                ? this
                : name == null || name.equals("") ? CURRENT : SPECIFIC;
    }
}
