package org.winterblade.minecraft.harmony.commands;

import com.google.common.base.Joiner;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Matt on 4/29/2016.
 */
public class RemoveSetCommand implements ICommand {
    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "removeSet";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch removeSet [-s|--silent] <Set Name> [<Set Name>...]";
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
        Set<String> sets = new HashSet<>();
        boolean silent = false;

        for(String arg : args) {
            if (!arg.startsWith("-")) {
                sets.add(arg);
            }

            if(arg.equals("-s") || arg.equals("--silent")) silent = true;
        }

        if(sets.size() <= 0) {
            if(!silent) sender.addChatMessage(new TextComponentString("A set name must be specified."));
            return;
        }

        for(String set : sets) {
            if (!CraftingHarmonicsMod.isValidSet(set)) {
                if(!silent) {
                    sender.addChatMessage(new TextComponentString(args[0] + " is not a valid set."));
                    sender.addChatMessage(new TextComponentString("Valid sets: " + Joiner.on(", ").join(CraftingHarmonicsMod.getAllSets())));
                }
                return;
            }
        }

        if(!CraftingHarmonicsMod.undoSets(sets.toArray(new String[sets.size()]))) {
            if(!silent) sender.addChatMessage(new TextComponentString("One or more of the sets: "
                    + Joiner.on(", ").join(sets) + " could not be removed."));
            return;
        }

        if(!silent) sender.addChatMessage(new TextComponentString(Joiner.on(", ").join(sets) + " removed."));
        CraftingHarmonicsMod.syncAllConfigs();
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     *
     * @param server The Minecraft server instance
     * @param sender The command sender who we are checking permission on
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return false;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return CraftingHarmonicsMod.getAllSets();
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

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
