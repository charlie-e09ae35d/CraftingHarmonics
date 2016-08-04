package org.winterblade.minecraft.harmony.integration.ticon.operations;

/**
 * Created by Matt on 8/3/2016.
 */

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
@Operation(name = "TConstruct.enableModifier", dependsOn = "tconstruct")
public class EnableModifier extends BasicOperation {
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

        if(modifier == null) throw new OperationException("TConstruct.enableModifier cannot find modifier with ID '" + what + "'.");

        aliases = ReflectedTinkerRegistry.getModifierAliasesFor(modifier);
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        LogHelper.info("Enabling Tinker's Construct modifier '" + what + "'.");
        ReflectedTinkerRegistry.addModifier(modifier, aliases);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        ReflectedTinkerRegistry.removeModifier(aliases);
    }
}