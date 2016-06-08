package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.matchers.BaseCooldownMatcher;

/**
 * Created by Matt on 5/22/2016.
 */
@Component(properties = "cooldown")
@PrioritizedObject(priority = Priority.LOWER)
public class CooldownMatcher extends BaseCooldownMatcher<Entity> implements IEntityMatcher {
    public CooldownMatcher(int cooldown) {
        super(cooldown);
    }

    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        return matches(entity, entity.getEntityWorld());
    }
}
