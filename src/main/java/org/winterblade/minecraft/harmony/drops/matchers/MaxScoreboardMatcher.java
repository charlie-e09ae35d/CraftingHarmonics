package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.dto.NameValuePair;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"maxScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MaxScoreboardMatcher extends BaseScoreboardMatcher implements IMobDropMatcher {
    public MaxScoreboardMatcher(NameValuePair<Integer> score) {super(score.getName(), Integer.MIN_VALUE, score.getValue());}

    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        if(evt.getSource() == null || evt.getSource().getEntity() == null) return BaseDropMatchResult.False;
        return matches(evt.getSource().getEntity().getEntityWorld(), evt.getSource().getEntity());
    }
}