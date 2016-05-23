package org.winterblade.minecraft.harmony.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by Matt on 5/22/2016.
 */
public class PotionRemovedHookTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(!transformedName.equals("net.minecraft.entity.EntityLivingBase")) return basicClass;

        boolean dev = name.equals(transformedName);

        LogHelper.info("Applying potion removed hook to " + transformedName + " (" + name + ")...");

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, 0);

        // Declare a few names:
        String fnName = dev ? "removeActivePotionEffect" : "func_184596_c";
        String activePotionsFieldName = dev ? "activePotionsMap" : "field_70713_bf";
        String potionEffectName = "net/minecraft/potion/PotionEffect";
        String elbName = transformedName.replace('.','/');

        for(MethodNode methodNode : classNode.methods)
        {
            if(!methodNode.name.equals(fnName)) continue;

            LogHelper.info("Patching removeActivePotionEffect (" + fnName + ")...");

            // Blow up the method.
            methodNode.instructions.clear();

            /**
             * Add our hook:
             *
             * This is just turning the function into:
             *      return MobPotionEffect.potionEffectHook(this.activePotionsMap.remove(potion));
             */
            try {
                methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
                methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
                methodNode.instructions.add(new FieldInsnNode(GETFIELD, elbName, activePotionsFieldName, "Ljava/util/Map;"));
                methodNode.instructions.add(new VarInsnNode(ALOAD, 1));
                methodNode.instructions.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
                methodNode.instructions.add(new TypeInsnNode(CHECKCAST, potionEffectName));
                methodNode.instructions.add(new MethodInsnNode(INVOKESTATIC,
                        "org/winterblade/minecraft/harmony/mobs/effects/MobPotionEffect",
                        "potionEffectHook",
                        "(L" + elbName + ";L" + potionEffectName + ";)L" + potionEffectName + ";",
                        false));
                methodNode.instructions.add(new InsnNode(ARETURN));
            }
            catch(Exception e) {
                LogHelper.info("Unable to patch removeActivePotionEffect", e);
                return basicClass;
            }

            LogHelper.info("Done patching removeActivePotionEffect...");
        }

        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);

            LogHelper.info("Writing updated " + transformedName);
            return classWriter.toByteArray();
        }
        catch (Exception e) {
            LogHelper.info("Unable to write updated " + transformedName, e);
            return basicClass;
        }
    }
}
