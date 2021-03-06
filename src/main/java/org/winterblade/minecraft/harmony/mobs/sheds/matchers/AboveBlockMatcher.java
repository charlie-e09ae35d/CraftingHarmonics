package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseAboveBlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/14/2016.
 */
@Component(properties = {"aboveBlock"})
@PrioritizedObject(priority = Priority.HIGH)
public class AboveBlockMatcher extends BaseAboveBlockMatcher implements IMobShedMatcher {
    public AboveBlockMatcher(@Nullable BlockMatcher matcher) {
        super(matcher);
    }

    @Override
    public BaseMatchResult isMatch(EntityLivingBase entity, ItemStack drop) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
