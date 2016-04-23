package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.CraftingHarmonicsMod;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;

import java.util.List;

/**
 * Created by Matt on 4/22/2016.
 */
public class GetResourceLocatorCommand implements ICommand {

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "getResourceLocator";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch getResourceLocator";
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
        if(!(sender instanceof EntityPlayer)) {
            sender.addChatMessage(new TextComponentString("Only players can use this command."));
            return;
        }

        // Get the player
        EntityPlayer player = (EntityPlayer)sender;
        Iterable<ItemStack> itemInHand = player.getHeldEquipment();

        // Get their item(s) in hand
        if(itemInHand == null || !itemInHand.iterator().hasNext()) {
            sender.addChatMessage(new TextComponentString("You need an item in your main hand to use this command."));
            return;
        }

        // Get the mainhand item...
        ItemStack mainHand = itemInHand.iterator().next();
        if(mainHand == null) {
            // Didn't we just check this?
            sender.addChatMessage(new TextComponentString("You need an item in your main hand to use this command."));
            return;
        }

        String resourceLocator = ItemRegistry.outputItemName(mainHand);
        CraftingHarmonicsMod.logger.info(resourceLocator);
        sender.addChatMessage(new TextComponentString("Locator (also logged): " + resourceLocator));
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

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}