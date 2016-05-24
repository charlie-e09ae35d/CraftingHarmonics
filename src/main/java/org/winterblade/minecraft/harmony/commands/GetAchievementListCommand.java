package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Matt on 5/23/2016.
 */
public class GetAchievementListCommand extends SubCommand {
    @Override
    public String getHelpText() {
        return "Lists off all available achievement IDs for use in the playerHasAchievement matcher.";
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "getAchievementList";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch getAchievementList";
    }

    /**
     * Callback for when the command is executed
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Output the list...
        AchievementList.ACHIEVEMENTS
                .stream()
                .map(a -> " - " + a.statId + " (" + a.getStatName().getUnformattedText() + ")")
                .forEach(LogHelper::info);

        sender.addChatMessage(new TextComponentString("The list has been sent to the console log."));
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *
     * @param args
     * @param index
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
