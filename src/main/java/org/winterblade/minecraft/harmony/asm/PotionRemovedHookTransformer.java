package org.winterblade.minecraft.harmony.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by Matt on 5/22/2016.
 */
public class PotionRemovedHookTransformer implements IClassTransformer {
    private String activePotionsFieldName;
    private String onFinishedPotionEffectName;
    private final String potionEffectName = "net/minecraft/potion/PotionEffect";
    private final String elbName = "net/minecraft/entity/EntityLivingBase";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(!transformedName.equals("net.minecraft.entity.EntityLivingBase")) return basicClass;

        boolean dev = name.equals(transformedName);

        LogHelper.info("Applying potion hooks to " + transformedName + "...");

        ClassNode classNode = new ClassNode();
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(classNode, 0);

        // Declare a few names:
        activePotionsFieldName = dev ? "activePotionsMap" : "field_70713_bf";
        onFinishedPotionEffectName = dev ? "onFinishedPotionEffect" : "func_70688_c";

        for(MethodNode methodNode : classNode.methods)
        {
            switch (methodNode.name) {
                case "removeActivePotionEffect":
                case "func_184596_c":
                    patchRemoveActivePotionEffect(methodNode);
                    break;
                case "curePotionEffects":
                    patchCurePotionEffects(methodNode);
                    break;
                case "updatePotionEffects":
                case "func_70679_bo":
                    patchUpdatePotionEffects(methodNode);
                    break;
                case "clearActivePotions":
                case "func_70674_bp":
                    patchClearActivePotions(methodNode);
                    break;
            }
        }

        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);

            LogHelper.info("Finished updating " + transformedName);
            return classWriter.toByteArray();
        }
        catch (Exception e) {
            LogHelper.error("Unable to write updated " + transformedName, e);
            return basicClass;
        }
    }

    private void patchRemoveActivePotionEffect(MethodNode methodNode) {
        LogHelper.info(" - Patching removeActivePotionEffect...");

        // Blow up the method.
        methodNode.instructions.clear();

        /**
         * Add our hook:
         *
         * This is just turning the function into:
         *      return MobPotionEffect.potionRemovedHook(this.activePotionsMap.remove(potion));
         */
        try {
            methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
            methodNode.instructions.add(new VarInsnNode(ALOAD, 0));
            methodNode.instructions.add(new FieldInsnNode(GETFIELD, elbName, activePotionsFieldName, "Ljava/util/Map;"));
            methodNode.instructions.add(new VarInsnNode(ALOAD, 1));
            methodNode.instructions.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
            methodNode.instructions.add(new TypeInsnNode(CHECKCAST, potionEffectName));
            methodNode.instructions.add(new MethodInsnNode(INVOKESTATIC,
                    "org/winterblade/minecraft/harmony/entities/effects/MobPotionEffect",
                    "potionRemovedHook",
                    "(L" + elbName + ";L" + potionEffectName + ";)L" + potionEffectName + ";",
                    false));
            methodNode.instructions.add(new InsnNode(ARETURN));
        }
        catch(Exception e) {
            LogHelper.error("Unable to patch removeActivePotionEffect", e);
        }
    }

    private void patchCurePotionEffects(MethodNode methodNode) {
        patchMethodAfterFinishedPotion(methodNode, "curePotionEffects", "potionCuredHook");
    }

    private void patchUpdatePotionEffects(MethodNode methodNode) {
        patchMethodAfterFinishedPotion(methodNode, "updatePotionEffects", "potionExpiredHook");
    }

    private void patchClearActivePotions(MethodNode methodNode) {
        LogHelper.info(" - Patching clearActivePotions...");

        // This one's a bit different, since we have to split apart the existing definition...
        // This happens right after iterator.next(), and it basically creates a new PotionEffect variable off that
        // which we pass in to our hook before giving it over to the actual method it was intended for.
        try {
            AbstractInsnNode targetNode = null;
            AbstractInsnNode[] insnNodes = methodNode.instructions.toArray();
            for (int i = 0; i < insnNodes.length; i++) {
                AbstractInsnNode instruction = insnNodes[i];
                if (instruction.getOpcode() != CHECKCAST || !((TypeInsnNode) instruction).desc.equals(potionEffectName)) continue;
                targetNode = insnNodes[i + 1];
            }

            if (targetNode == null) {
                LogHelper.error("Unable to patch clearActivePotions; couldn't find line to target.");
                return;
            }

            InsnList toInsert = new InsnList();
            // There's going to be an extra copy  of 'this' floating around on the stack for a minute...
            toInsert.add(new VarInsnNode(ASTORE, 2)); // Store the effect we just popped

            // Pass it to our hook
            toInsert.add(new VarInsnNode(ALOAD, 0));
            toInsert.add(new VarInsnNode(ALOAD, 2));
            toInsert.add(new MethodInsnNode(INVOKESTATIC,
                    "org/winterblade/minecraft/harmony/entities/effects/MobPotionEffect",
                    "potionCuredHook",
                    "(L" + elbName + ";L" + potionEffectName + ";)V",
                    false));

            // Now load it back up to make the state all fine again
            toInsert.add(new VarInsnNode(ALOAD, 2));
            // The copy of 'this' from above finally gets used around here...

            methodNode.instructions.insertBefore(targetNode, toInsert);
        } catch(Exception e) {
            LogHelper.error("Unable to patch clearActivePotions...",e);
        }
    }

    /**
     * Patch the given method with the specified hook
     * @param methodNode    The method to operate on
     * @param name          The name of the method (for logging)
     * @param hook          The hook to insert
     */
    private void patchMethodAfterFinishedPotion(MethodNode methodNode, String name, String hook) {
        LogHelper.info(" - Patching " + name + "...");

        try {
            AbstractInsnNode targetNode = targetAfterFinishedPotionEffect(methodNode);

            if (targetNode == null) {
                LogHelper.error("Unable to patch " + name + "; couldn't find line to target.");
                return;
            }

            insertHook(methodNode, targetNode, hook);
        } catch(Exception e) {
            LogHelper.error("Unable to patch " + name + "...",e);
        }
    }

    /**
     * Inserts a given hook, passing the Entity and the effect.
     * @param methodNode    The method to operate on
     * @param targetNode    The spot to insert the hook before
     * @param hookName      The name of the hook to call
     */
    private void insertHook(MethodNode methodNode, AbstractInsnNode targetNode, String hookName) {
        InsnList toInsert = new InsnList();
        toInsert.add(new VarInsnNode(ALOAD, 0));
        toInsert.add(new VarInsnNode(ALOAD, 3));
        toInsert.add(new MethodInsnNode(INVOKESTATIC,
                "org/winterblade/minecraft/harmony/entities/effects/MobPotionEffect",
                hookName,
                "(L" + elbName + ";L" + potionEffectName + ";)V",
                false));
        methodNode.instructions.insertBefore(targetNode, toInsert);
    }

    /**
     * Gets a target node one line after 'onFinishedPotionEffect'
     * @param methodNode    The method to look through
     * @return              The target node, or null if it wasn't found.
     */
    @Nullable
    private AbstractInsnNode targetAfterFinishedPotionEffect(MethodNode methodNode) {
        AbstractInsnNode[] insnNodes = methodNode.instructions.toArray();
        for (int i = 0; i < insnNodes.length; i++) {
            AbstractInsnNode instruction = insnNodes[i];
            if (instruction.getOpcode() != INVOKEVIRTUAL || !((MethodInsnNode) instruction).name.equals(onFinishedPotionEffectName)) continue;
            return insnNodes[i + 1];
        }
        return null;
    }
}
