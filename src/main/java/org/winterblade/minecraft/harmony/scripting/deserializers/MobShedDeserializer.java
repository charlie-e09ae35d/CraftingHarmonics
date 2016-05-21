package org.winterblade.minecraft.harmony.scripting.deserializers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 5/12/2016.
 */
@ScriptObjectDeserializer(deserializes = MobShed.class)
public class MobShedDeserializer extends BaseDropDeserializer<EntityLivingBase, IMobShedMatcher, MobShed> {
    public MobShedDeserializer() {super(IMobShedMatcher.class);}

    @Override
    protected MobShed newInstance() {
        return new MobShed();
    }
}
