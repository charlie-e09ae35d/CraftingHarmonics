package org.winterblade.minecraft.harmony.callbacks.mobs;

import io.netty.buffer.ByteBuf;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.winterblade.minecraft.harmony.api.IEntityCallback;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.scripting.deserializers.BaseMirroredDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/22/2016.
 */
public class CommandCallback implements IEntityCallback {
    private String command;
    private String name;

    @Override
    public void apply(EntityLivingBase target, World world) {
        if(target.getEntityWorld().isRemote) return;

        String specificCommand = command.replaceAll("@p", target.getName());

        // Method train of doom...
        int result = FMLCommonHandler.instance()
                        .getMinecraftServerInstance()
                        .getCommandManager()
                        .executeCommand(
                            new CommandCallbackSender(target, name), specificCommand
                        );

        // TODO: "onError" callback.
        LogHelper.info("Ran '" + specificCommand + "' from '" + target.getName() + "'.  Result code: " + result);
        
    }

    @ScriptObjectDeserializer(deserializes = CommandCallback.class)
    public static class Deserializer extends BaseMirroredDeserializer {
        @Override
        protected Object DeserializeMirror(ScriptObjectMirror mirror) {
            CommandCallback output = new CommandCallback();

            output.command = mirror.get("command").toString();
            output.name = mirror.containsKey("name") ? mirror.get("name").toString() : null;

            return output;
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