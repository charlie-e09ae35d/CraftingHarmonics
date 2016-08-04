package org.winterblade.minecraft.harmony.integration.ticon.operations;

import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;
import org.winterblade.minecraft.harmony.integration.ticon.ReflectedTinkerRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;

import java.util.List;

/**
 * Created by Matt on 8/3/2016.
 */
@Operation(name = "TConstruct.disableModifier", dependsOn = "tconstruct")
public class DisableModifier extends BasicOperation {
    /*
     * Serialzied properties
     */
    private String what;

    /*
     * Computed properties
     */
    private transient IModifier modifier;
    private transient List<String> aliases;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        modifier = TinkerRegistry.getModifier(what);

        if(modifier == null) throw new OperationException("TConstruct.disableModifier cannot find modifier with ID '" + what + "'.");

        aliases = ReflectedTinkerRegistry.getModifierAliasesFor(modifier);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Disabling Tinker's Construct modifier '" + what + "'.");
        ReflectedTinkerRegistry.removeModifier(aliases);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        ReflectedTinkerRegistry.addModifier(modifier, aliases);
    }
}
