package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.blocks.BlockMatcher;
import org.winterblade.minecraft.harmony.common.matchers.BaseBlockMatcher;

import javax.annotation.Nullable;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"insideBlock"})
@PrioritizedObject(priority = Priority.HIGH)
public class InBlockMatcher extends BaseBlockMatcher implements IEntityMatcher {
    public InBlockMatcher(@Nullable BlockMatcher matcher) {
        super(matcher);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param metadata The event metadata
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity.getEntityWorld(), entity.getPosition());
    }
}
