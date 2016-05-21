package org.winterblade.minecraft.harmony.commands;

import com.google.common.base.Joiner;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.potion.Potion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fluids.FluidRegistry;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Matt on 5/21/2016.
 */
public class GetPotionsCommand extends SubCommand {
    @Override
    public String getHelpText() {
        return "Lists available potions for use in operations.";
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "getPotions";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch getPotions";
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
        String potions = "Registered potions: " + Joiner.on(", ").join(Potion.REGISTRY.getKeys());

        sender.addChatMessage(new TextComponentString(potions));
        LogHelper.info(potions);
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
