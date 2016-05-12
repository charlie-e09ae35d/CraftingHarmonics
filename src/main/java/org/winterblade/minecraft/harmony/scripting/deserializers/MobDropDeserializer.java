package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.crafting.ComponentRegistry;
import org.winterblade.minecraft.harmony.crafting.ItemMissingException;
import org.winterblade.minecraft.harmony.crafting.ItemRegistry;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;
import org.winterblade.minecraft.harmony.utility.LogHelper;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;
import org.winterblade.minecraft.scripting.api.ScriptObjectDeserializer;

import java.util.List;

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
    protected void update(ScriptObjectMirror mirror, MobDrop drop) {
        if(mirror.containsKey("lootingMultiplier")) {
            drop.setLootingMultiplier((Double) mirror.get("lootingMultiplier"));
        }
    }
}
