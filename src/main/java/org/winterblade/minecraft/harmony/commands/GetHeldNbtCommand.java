package org.winterblade.minecraft.harmony.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import java.util.List;

/**
 * Created by Matt on 4/7/2016.
 */
public class GetHeldNbtCommand extends SubCommand {
    @Override
    public String getCommandName() {
        return "getHeldNBT";
    }

    @Override
    public String getHelpText() {
        return "Gets the NBT of your current held main hand item in JSON format.";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/ch getHeldNBT";
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

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

        if(!mainHand.hasTagCompound()) {
            sender.addChatMessage(new TextComponentString("This item doesn't have an NBT tag."));
            return;
        }

        String nbtText = mainHand.getTagCompound().toString();
        LogHelper.info(nbtText);
        sender.addChatMessage(new TextComponentString("NBT (also logged): " + nbtText));
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

}
