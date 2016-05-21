package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseNotMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"not"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class NotMatcher extends BaseNotMatcher<EntityLivingBase, ItemStack, IMobShedMatcher, MobShed> implements IMobShedMatcher {
    public NotMatcher(MobShed composite) {
        super(composite);
    }
}