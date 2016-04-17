package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Created by Matt on 4/7/2016.
 */
public class CommandHandler implements ICommand {
    private static final List<String> aliases = new ArrayList<>();
    private static final Map<String, ICommand> subcommands = new HashMap<>();

    static {
        aliases.add("ch");
        subcommands.put("getHeldNBT", new GetHeldNbtCommand());
        subcommands.put("reload", new ReloadCommand());
    }

    @Override
    public String getCommandName() {
        return "ch";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        String helpText = "ch <help";
        for(String command : subcommands.keySet()) {
            helpText += "|" + command;
        }

        return helpText + ">";
    }

    @Override
    public List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            sender.addChatMessage(new TextComponentString("Needs a subcommand; usage: /" + getCommandUsage(sender)));
            return;
        }

        String[] subArgs = Arrays.copyOfRange(args,1,args.length);
        getSubCommand(args[0]).execute(server, sender, subArgs);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return server.isSinglePlayer() || sender.canCommandSenderUseCommand(2,"");
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 0) {
            return new ArrayList<>(subcommands.keySet());
        }

        String[] subArgs = Arrays.copyOfRange(args,1,args.length);
        return getSubCommand(args[1]).getTabCompletionOptions(server, sender, subArgs, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        if(index == 0 || args.length == 0) return false;

        String[] subArgs = Arrays.copyOfRange(args,1,args.length);
        return getSubCommand(args[1]).isUsernameIndex(subArgs, index-1);
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return 0;
    }

    /**
     * Gets the subcommand for this command
     * @param name  The name of the command to get
     * @return      The command, or a no-op if it doesn't exist.
     */
    private ICommand getSubCommand(String name) {
        if(name == null || name.equals("") || !subcommands.containsKey(name)) return new NoOpCommand();
        return subcommands.get(name);
    }
}
