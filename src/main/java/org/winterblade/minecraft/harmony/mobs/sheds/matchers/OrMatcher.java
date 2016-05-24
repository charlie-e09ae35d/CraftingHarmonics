package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseOrMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"or"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class OrMatcher extends BaseOrMatcher<EntityLivingBase, ItemStack, IMobShedMatcher, MobShed> implements IMobShedMatcher {
    public OrMatcher(MobShed[] composites) {
        super(composites);
    }
}