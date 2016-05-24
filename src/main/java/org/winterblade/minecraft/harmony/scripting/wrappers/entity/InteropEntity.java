package org.winterblade.minecraft.harmony.scripting.wrappers.entity;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.ScriptInterop;

/**
 * Created by Matt on 5/22/2016.
 */
@ScriptInterop(wraps = Entity.class)
public class InteropEntity {
    private final Entity entity;

    public InteropEntity(Entity entity) {
        this.entity = entity;
    }

    public String getName() {
        return entity.getName();
    }

    public String getUniqueID() {
        return entity.getUniqueID().toString();
    }

    // TODO: More interop
}
