package org.winterblade.minecraft.harmony.tileentities.matchers.quest;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.quests.matchers.BaseIsHardcoreMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = {"questingIsHardcore"})
@PrioritizedObject(priority = Priority.HIGH)
public class IsHardcoreMatcher extends BaseIsHardcoreMatcher implements ITileEntityMatcher {
    public IsHardcoreMatcher(boolean isHardcore) {
        super(isHardcore);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        return matches();
    }
}