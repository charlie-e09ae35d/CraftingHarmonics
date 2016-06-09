package org.winterblade.minecraft.harmony.tileentities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityTargetModifier;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityCallback;
import org.winterblade.minecraft.harmony.api.tileentities.TileEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.harmony.tileentities.BaseTileEntityCallback;

import java.util.List;

/**
 * Created by Matt on 6/8/2016.
 */
@TileEntityCallback(name = "targetEntities")
public class TargetEntitiesCallback extends BaseTileEntityCallback {
    private IEntityCallback[] perTarget;
    private ITileEntityCallback[] onEachSuccess;
    private ITileEntityCallback[] onSuccess;
    private ITileEntityCallback[] onFailure;
    private ITileEntityCallback[] onComplete;
    private IEntityTargetModifier targetModifier = null;

    /**
     * Apply an action to the target.
     *
     * @param source The target to apply to.
     * @param data   Any event data to deal with.
     */
    @Override
    protected void applyTo(TileEntity source, CallbackMetadata data) {
        List<Entity> targets = targetModifier.getTargets(source, data);

        // If we didn't match anything...
        if(targets.size() <= 0) {
            runCallbacks(onFailure, source);
            runCallbacks(onComplete, source);
            return;
        }

        // Otherwise, loop...
        for(Entity target : targets) {
            for(IEntityCallback callback : perTarget) {
                callback.apply(target, data);
            }
            runCallbacks(onEachSuccess, source);
        }

        runCallbacks(onSuccess, source);
        runCallbacks(onComplete, source);
    }

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                IEntityTargetModifier.class}, mirror);

        // Check our target modifier first, because we have to have one...
        List<IEntityTargetModifier> modifiers = registry.getComponentsOf(IEntityTargetModifier.class);
        if(0 < modifiers.size()) {
            targetModifier = modifiers.get(0);
        } else {
            throw new RuntimeException("Tile Entity's targetEntities callback must have a target modifier.");
        }
    }
}
