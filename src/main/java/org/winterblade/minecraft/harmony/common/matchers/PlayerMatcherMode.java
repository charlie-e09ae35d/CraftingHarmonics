package org.winterblade.minecraft.harmony.common.matchers;

import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

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

    @ScriptObjectDeserializer(deserializes = PlayerMatcherMode.class)
    public static class Deserializer implements IScriptObjectDeserializer {

        @Override
        public Object Deserialize(Object input) {
            if(!String.class.isAssignableFrom(input.getClass())) return SPECIFIC;

            switch (input.toString().toUpperCase()) {
                case "ALL":
                    return PlayerMatcherMode.ALL;
                case "ALLONLINE":
                    return PlayerMatcherMode.ALLONLINE;
                case "ANY":
                    return PlayerMatcherMode.ANY;
                case "ANYONLINE":
                    return PlayerMatcherMode.ANYONLINE;
                default:
                    return PlayerMatcherMode.SPECIFIC;
            }
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
