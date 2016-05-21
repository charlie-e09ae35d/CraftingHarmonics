package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseScoreboardMatcher;
import org.winterblade.minecraft.harmony.dto.NameValuePair;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"minScore"})
@PrioritizedObject(priority = Priority.HIGH)
public class MinScoreboardMatcher extends BaseScoreboardMatcher implements IMobShedMatcher {
    public MinScoreboardMatcher(NameValuePair<Integer> score) {super(score.getName(), score.getValue(), Integer.MAX_VALUE);}

    @Override
    public BaseMatchResult isMatch(EntityLivingBase evt, ItemStack drop) {
        return matches(evt.getEntityWorld(), evt);
    }
}