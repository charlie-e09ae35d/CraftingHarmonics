package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.scripting.NashornConfigProcessor;

import java.util.List;

/**
 * Created by Matt on 4/16/2016.
 */
public class ReloadCommand extends SubCommand {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "reload";
    }

    @Override
    public String getHelpText() {
        return "Reloads the configuration from disk and syncs configs out to all connected players.";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch reload";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    /**
     * Callback for when the command is executed
     *
     * @param server The Minecraft server instance
     * @param sender The source of the command invocation
     * @param args   The arguments that were passed
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        CraftingHarmonicsMod.reloadConfigs(server, (boolean success) -> {
            if(success) {
                sender.addChatMessage(new TextComponentString("Configuration reloaded successfully."));
                return;
            }

            NashornConfigProcessor.getInstance().reportErrorsTo(sender);
        });
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *
     * @param args      The args provided
     * @param index     The index we're at
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
