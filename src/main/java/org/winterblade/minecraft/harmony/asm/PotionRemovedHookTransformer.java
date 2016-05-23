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
    private String activePotionsFieldName;
    private final String potionEffectName = "net/minecraft/potion/PotionEffect";
    private final String elbName = "net/minecraft/entity/EntityLivingBase";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(!transformedName.equals("net.minecraft.entity.EntityLivingBase")) return basicClass;

        boolean dev = name.equals(transformedName);

        LogHelper.info("Applying potion removed hook to " + transformedName + " (" + name + ")...");

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, 0);

        // Declare a few names:
        activePotionsFieldName = dev ? "activePotionsMap" : "field_70713_bf";

        for(MethodNode methodNode : classNode.methods)
        {
            switch (methodNode.name) {
                case "removeActivePotionEffect":
                case "func_184596_c":
                    patchRemoveActivePotionEffect(methodNode);
                    break;
                case "curePotionEffects":
                // case "???":
                    patchCurePotionEffects(methodNode);
                    break;
            }
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

    private void patchRemoveActivePotionEffect(MethodNode methodNode) {
        LogHelper.info("Patching removeActivePotionEffect...");

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
            return;
        }

        LogHelper.info("Done patching removeActivePotionEffect...");
    }

    private void patchCurePotionEffects(MethodNode methodNode) {
        LogHelper.info("Patching curePotionEffects...");

        try {
            AbstractInsnNode targetNode = null;
            AbstractInsnNode[] insnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < insnNodes.length; i++) {
                AbstractInsnNode instruction = insnNodes[i];
                if (instruction.getOpcode() != PUTFIELD || !((FieldInsnNode) instruction).desc.equals("Z")) continue;
                targetNode = insnNodes[i + 1];
                break;
            }

            if (targetNode == null) {
                LogHelper.warn("Unable to patch curePotionEffects; couldn't find line to replace.");
                return;
            }

            InsnList toInsert = new InsnList();
            toInsert.add(new VarInsnNode(ALOAD, 0));
            toInsert.add(new VarInsnNode(ALOAD, 3));
            toInsert.add(new MethodInsnNode(INVOKESTATIC,
                    "org/winterblade/minecraft/harmony/mobs/effects/MobPotionEffect",
                    "potionCuredHook",
                    "(L" + elbName + ";L" + potionEffectName + ";)V",
                    false));
            methodNode.instructions.insertBefore(targetNode, toInsert);
        } catch(Exception e) {
            LogHelper.warn("Unable to patch curePotionEffects...",e);
        }

        LogHelper.info("Done patching curePotionEffects...");
    }
}
