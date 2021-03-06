package org.winterblade.minecraft.harmony.commands;

import com.google.common.base.Joiner;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.List;

/**
 * Created by Matt on 4/23/2016.
 */
public class ListFluidsCommand extends SubCommand {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "listFluids";
    }

    @Override
    public String getHelpText() {
        return "Lists all fluids currently in the fluid registry.";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch listFluids";
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
        String fluids = "Registered fluids: " + Joiner.on(", ").join(FluidRegistry.getRegisteredFluids().keySet());

        sender.addChatMessage(new TextComponentString(fluids));
        LogHelper.info(fluids);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
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
