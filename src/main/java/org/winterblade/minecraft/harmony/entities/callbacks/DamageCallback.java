package org.winterblade.minecraft.harmony.entities.callbacks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import org.winterblade.minecraft.harmony.api.entities.EntityCallback;
import org.winterblade.minecraft.harmony.api.entities.IEntityCallback;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

/**
 * Created by Matt on 5/26/2016.
 */
@EntityCallback(name = "damage")
public class DamageCallback extends BaseEntityCallback {
    /*
     * Serialized properties
     */
    private float amount;
    private String damageType;
    private boolean isUnblockable;
    private boolean isAbsolute;
    private boolean isCreative;
    private IEntityCallback[] onInvulnerable;
    private IEntityCallback[] onDamage;
    private IEntityCallback[] onDeath;
    private IEntityCallback[] onComplete;

    /*
     * Computed properties
     */
    private transient DamageSource source;

    /**
     * Allows the instance to do any last minute updating it needs to, if necessary
     *
     * @param mirror The mirror to update from
     */
    @Override
    protected void finishDeserialization(ScriptObjectMirror mirror) throws RuntimeException {
        if(amount < 0) {
            LogHelper.warn("Cannot deal less than 0 damage.");
            amount = 0;
        }

        if(damageType == null) {
            damageType = "generic";
        }

        this.source = new DamageSource(damageType);
        if(isUnblockable) this.source.setDamageBypassesArmor();
        if(isAbsolute) this.source.setDamageIsAbsolute();
        if(isCreative) this.source.setDamageAllowedInCreativeMode();
    }

    @Override
    protected void applyTo(Entity target, CallbackMetadata data) {
        // TIL: You can 'attack' all entities.
        target.attackEntityFrom(source, amount);

        // Run our callbacks...
        if(target.isEntityInvulnerable(source)) {
            runCallbacks(onInvulnerable, target);
        } else if(target.isEntityAlive()) {
            runCallbacks(onDamage, target);
        } else {
            runCallbacks(onDeath, target);
        }

        runCallbacks(onComplete, target);
    }
}
