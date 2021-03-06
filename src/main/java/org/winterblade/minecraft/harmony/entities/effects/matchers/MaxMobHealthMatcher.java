package org.winterblade.minecraft.harmony.entities.effects.matchers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"maxMobHealth"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MaxMobHealthMatcher implements IEntityMatcher {
    private final float health;

    public MaxMobHealthMatcher(float health) {
        this.health = health;
    };

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entity The event to match
     * @param metadata     Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(Entity entity, CallbackMetadata metadata) {
        if(!EntityLivingBase.class.isAssignableFrom(entity.getClass())) return BaseMatchResult.False;
        return new BaseMatchResult(((EntityLivingBase)entity).getHealth() <= health);
    }
}
