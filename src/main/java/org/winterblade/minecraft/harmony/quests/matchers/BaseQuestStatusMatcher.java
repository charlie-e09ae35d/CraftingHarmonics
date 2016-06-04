package org.winterblade.minecraft.harmony.quests.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.questing.QuestStatus;
import org.winterblade.minecraft.harmony.common.matchers.PlayerMatcherMode;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/30/2016.
 */
public abstract class BaseQuestStatusMatcher {
    protected final QuestStatusMatchData data;
    protected final PlayerMatcherMode mode;

    protected BaseQuestStatusMatcher(QuestStatusMatchData data) {
        this.data = data;
        mode = PlayerMatcherMode.convert(data.getPlayer());
    }

    protected BaseMatchResult matches(@Nullable Entity entity) {
        // Allows for checking drops if any/all players are on the quest, even if the current target isn't a player...
        if(mode == PlayerMatcherMode.CURRENT
                && (entity == null || !EntityPlayerMP.class.isAssignableFrom(entity.getClass()))) return BaseMatchResult.False;

        switch (mode) {
            case CURRENT:
                return matches(QuestRegistry.instance.getQuestStatus(data.getName(), (EntityPlayerMP) entity));
            case SPECIFIC:
                // This will only work if the player's online..
                return matches(QuestRegistry.instance.getQuestStatus(data.getName(),
                        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(data.getPlayer())));
            case ALL:
            case ALLONLINE:
                return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList().stream()
                        .allMatch(p -> QuestRegistry.instance.getQuestStatus(data.getName(), p) == data.getStatus())
                            ? BaseMatchResult.True
                            : BaseMatchResult.False;
            case ANY:
            case ANYONLINE:
                return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList().stream()
                        .anyMatch(p -> QuestRegistry.instance.getQuestStatus(data.getName(), p) == data.getStatus())
                        ? BaseMatchResult.True
                        : BaseMatchResult.False;
        }
        return BaseMatchResult.False;
    }

    private BaseMatchResult matches(QuestStatus status) {
        return status == data.getStatus() ? BaseMatchResult.True : BaseMatchResult.False;
    }

    public static class QuestStatusMatchData {
        private String name;
        private QuestStatus status;
        private String player;

        public String getName() {
            return name;
        }

        public QuestStatus getStatus() {
            return status;
        }

        public String getPlayer() {
            return player;
        }
    }
}
