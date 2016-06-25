package org.winterblade.minecraft.harmony.items.operations;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.api.BasicOperation;
import org.winterblade.minecraft.harmony.api.Operation;
import org.winterblade.minecraft.harmony.api.OperationException;
import org.winterblade.minecraft.harmony.api.crafting.RecipeInput;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.items.ItemRegistry;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.scripting.api.IScriptContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 6/24/2016.
 */
@Operation(name = "preventItem")
public class PreventItemOperation extends BasicOperation {
    /*
     * Serialized properties
     */
    private RecipeInput[] what;
    private IEntityCallback[] onBlock;

    /*
     * Computed properties
     */
    private transient List<IEntityMatcher> matchers;
    private transient ItemRegistry.ItemBlockData data;

    /**
     * Called to initialize the set
     *
     * @throws OperationException If something went wrong
     */
    @Override
    public void init() throws OperationException {
        if(what == null || what.length <= 0) throw new OperationException("preventItem must have at least one block to prevent ('what').");
        if(onBlock == null) onBlock = new IEntityCallback[0];
        if(matchers == null) matchers = new ArrayList<>();
    }

    /**
     * Called to apply the set (if not player-specific)
     */
    @Override
    public void apply() {
        data = ItemRegistry.instance.preventUse(what, onBlock, matchers);
    }

    /**
     * Called to remove the set (if not player-specific)
     */
    @Override
    public void undo() {
        ItemRegistry.instance.allowUse(data);
    }

    /**
     * Used to convert the provided operation from the file into the given recipe.
     *
     * @param context
     * @param data    The operation data
     */
    @Override
    protected void readData(IScriptContext context, ScriptObjectMirror data) throws OperationException {
        super.readData(context, data);

        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                IEntityMatcher.class}, data);
        matchers = registry.getComponentsOf(IEntityMatcher.class);
    }
}
