package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

/**
 * Created by Matt on 4/9/2016.
 */
@ScriptObjectDeserializer(deserializes = MobDrop.class)
public class MobDropDeserializer extends BaseDropDeserializer<LivingDropsEvent, IMobDropMatcher, MobDrop> {
    public MobDropDeserializer() {super(IMobDropMatcher.class);}

    @Override
    protected MobDrop newInstance() {
        return new MobDrop();
    }

    @Override
    protected void updateExtraProps(ScriptObjectMirror mirror, MobDrop drop) {
        if(mirror.containsKey("lootingMultiplier")) {
            drop.setLootingMultiplier((Double) mirror.get("lootingMultiplier"));
        }
    }
}
