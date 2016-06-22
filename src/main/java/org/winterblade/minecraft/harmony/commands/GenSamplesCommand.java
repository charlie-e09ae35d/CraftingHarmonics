package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.config.ConfigManager;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Matt on 6/21/2016.
 */
public class GenSamplesCommand extends SubCommand {
    @Override
    public String getHelpText() {
        return "Tries to regenerate the sample config files.";
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "genSamples";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch genSamples";
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
        if(!server.isSinglePlayer()) {
            sender.addChatMessage(new TextComponentString("You may only regenerate the config files on a single-player instance."));
            return;
        }

        CraftingHarmonicsMod.getConfigManager().outputSamples();
        sender.addChatMessage(new TextComponentString("Sample files regenerated."));
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
