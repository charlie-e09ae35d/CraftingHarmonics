package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseOrMatcher;
import org.winterblade.minecraft.harmony.mobs.drops.MobDrop;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher extends BaseOrMatcher<LivingDropsEvent, ItemStack, IMobDropMatcher, MobDrop> implements IMobDropMatcher {
    public OrMatcher(MobDrop[] composites) {
        super(composites);
    }
}

