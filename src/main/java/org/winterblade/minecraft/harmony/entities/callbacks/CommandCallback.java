package org.winterblade.minecraft.harmony.entities.callbacks;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/22/2016.
 */
@EntityCallback(name = "runPlayerCommand")
public class CommandCallback extends BaseEntityCallback {
    private String[] command;
    private String name;

    @Override
    public void applyTo(Entity target, CallbackMetadata data) {
        if(target.getEntityWorld().isRemote) return;
        if(!EntityLivingBase.class.isAssignableFrom(target.getClass())) {
            LogHelper.info("Not running command '{}' on target as target is not a living entity ({})", command, target.getClass().getName());
            return;
        }

        if(name != null) name = name.replaceAll("@p", target.getDisplayName().getUnformattedText());

        CommandCallbackSender sender = new CommandCallbackSender((EntityLivingBase) target, name);

        for (String s : command) {
            String specificCommand = s.replaceAll("@p", target.getName());

            // Method train of doom...
            int result = FMLCommonHandler.instance()
                    .getMinecraftServerInstance()
                    .getCommandManager()
                    .executeCommand(
                            sender, specificCommand
                    );

            // TODO: "onError" callback.
            LogHelper.info("Ran '{}' from '{}'.  Result code: {}", specificCommand, target.getName(), result);
        }
    }

    /**
     * Wrapping implementation of the command sender.
     */
    private static class CommandCallbackSender extends CommandBlockBaseLogic
    {
        private final String sender;
        private final EntityLivingBase wrappedEntity;

        CommandCallbackSender(EntityLivingBase wrappedEntity, @Nullable String name) {
            this.wrappedEntity = wrappedEntity;

            sender = name;
        }

        @Override
        public void updateCommand() {

        }

        /**
         * Currently this returns 0 for the traditional command block, and 1 for the minecart command block
         */
        @Override
        public int getCommandBlockType() {
            return 0;
        }

        /**
         * Fills in information about the command block for the packet. X/Y/Z for the minecart version, and entityId for the
         * traditional version
         *
         * @param buf
         */
        @Override
        public void fillInInfo(ByteBuf buf) {}

        /**
         * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
         * the coordinates 0, 0, 0
         */
        @Override
        public BlockPos getPosition() {
            return wrappedEntity.getPosition();
        }

        /**
         * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
         * 0.0D, 0.0D
         */
        @Override
        public Vec3d getPositionVector() {
            return wrappedEntity.getPositionVector();
        }

        /**
         * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
         * the overworld
         */
        @Override
        public World getEntityWorld() {
            return wrappedEntity.getEntityWorld();
        }

        /**
         * Returns the entity associated with the command sender. MAY BE NULL!
         */
        @Nullable
        @Override
        public Entity getCommandSenderEntity() {
            return null;
        }

        /**
         * Get the Minecraft server instance
         */
        @Nullable
        @Override
        public MinecraftServer getServer() {
            return FMLCommonHandler.instance().getMinecraftServerInstance();
        }

        /**
         * Get the name of this object. For players this returns their username
         */
        @Override
        public String getName() {
            return sender != null ? sender : super.getName();
        }
    }
}