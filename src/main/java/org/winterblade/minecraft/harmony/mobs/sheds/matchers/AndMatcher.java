package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseAndMatcher;
import org.winterblade.minecraft.harmony.mobs.sheds.MobShed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/12/2016.
 */
@Component(properties = {"and"})
@PrioritizedObject(priority = Priority.MEDIUM)
public class AndMatcher extends BaseAndMatcher<EntityLivingBase, ItemStack, IMobShedMatcher, MobShed> implements IMobShedMatcher {
    public AndMatcher(MobShed[] composites) {
        super(composites);
    }
}