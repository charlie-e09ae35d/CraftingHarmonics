package org.winterblade.minecraft.harmony.tileentities.matchers;

import net.minecraft.tileentity.TileEntity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.tileentities.ITileEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseCooldownMatcher;

/**
 * Created by Matt on 6/4/2016.
 */
@Component(properties = "cooldown")
@PrioritizedObject(priority = Priority.LOWER)
public class CooldownMatcher extends BaseCooldownMatcher<TileEntity> implements ITileEntityMatcher {
    public CooldownMatcher(int cooldown) {
        super(cooldown);
    }

    @Override
    public BaseMatchResult isMatch(TileEntity tileEntity, CallbackMetadata metadata) {
        return matches(tileEntity, tileEntity.getWorld());
    }
}