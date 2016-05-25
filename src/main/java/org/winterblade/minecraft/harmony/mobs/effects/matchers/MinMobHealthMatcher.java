package org.winterblade.minecraft.harmony.mobs.effects.matchers;

import net.minecraft.entity.EntityLivingBase;
import org.winterblade.minecraft.harmony.api.BaseMatchResult;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.entities.IEntityMatcherData;
import org.winterblade.minecraft.harmony.api.mobs.effects.IEntityMatcher;

/**
 * Created by Matt on 5/21/2016.
 */
@Component(properties = {"minMobHealth"})
@PrioritizedObject(priority = Priority.HIGHER)
public class MinMobHealthMatcher implements IEntityMatcher {
    private final float health;

    public MinMobHealthMatcher(float health) {
        this.health = health;
    };

    /**
     * Should return true if this matcher matches the given event
     *
     * @param entityLiving The event to match
     * @param metadata     Event metadata.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseMatchResult isMatch(EntityLivingBase entityLiving, IEntityMatcherData metadata) {
        return new BaseMatchResult(health <= entityLiving.getHealth());
    }
}