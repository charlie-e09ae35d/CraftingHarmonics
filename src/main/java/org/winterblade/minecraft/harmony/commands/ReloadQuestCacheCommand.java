package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.quests.QuestRegistry;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Matt on 6/3/2016.
 */
public class ReloadQuestCacheCommand extends SubCommand {
    @Override
    public String getHelpText() {
        return "Resets the quest cache, in case new quests have been added that the quest system doesn't recognize.";
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "reloadQuestCache";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch reloadQuestCache";
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
        QuestRegistry.instance.resetCache();
        sender.addChatMessage(new TextComponentString("Quest cache reloaded."));
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
