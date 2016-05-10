package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.util.List;

/**
 * Created by Matt on 5/10/2016.
 */
public abstract class SubCommand implements ICommand {
    public List<String> getCommandAliases() {
        return null;
    }

    public abstract String getHelpText();

    /**
     * Check if the given ICommandSender has permission to execute this command
     *
     * @param server The Minecraft server instance
     * @param sender The command sender who we are checking permission on
     */
    @Override
    public final boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return false;
    }

    @Override
    public final int compareTo(ICommand o) {
        return 0;
    }
}
