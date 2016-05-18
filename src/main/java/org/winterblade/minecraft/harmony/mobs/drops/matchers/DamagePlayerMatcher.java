package org.winterblade.minecraft.harmony.mobs.drops.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.drops.matchers.BaseDamageEntityMatcher;

/**
 * Created by Matt on 5/16/2016.
 */
@Component(properties = {"damagePlayer", "playerDamageType", "playerDamageIsUnblockable", "playerDamageIsAbsolute", "playerDamageIsCreative"})
@PrioritizedObject(priority = Priority.LOWEST) // Only bother creating it if we're going through with the event
public class DamagePlayerMatcher extends BaseDamageEntityMatcher implements IMobDropMatcher {
    public DamagePlayerMatcher(float health) {
        this(health, "generic");
    }

    public DamagePlayerMatcher(float health, String damageType) {
        this(health, damageType, false);
    }

    public DamagePlayerMatcher(float health, String damageType, boolean isUnblockable) {
        this(health, damageType, isUnblockable, false);
    }

    public DamagePlayerMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute) {
        this(health, damageType, isUnblockable, isAbsolute, false);
    }

    public DamagePlayerMatcher(float health, String damageType, boolean isUnblockable, boolean isAbsolute, boolean isCreative) {
        super(health, damageType, isUnblockable, isAbsolute, isCreative);
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @param drop         The dropped item; this can be modified.
     * @return True if it should match; false otherwise
     */
    @Override
    public BaseDropMatchResult isMatch(LivingDropsEvent evt, ItemStack drop) {
        if(evt.getSource() == null) return BaseDropMatchResult.True;
        return damageEntity(evt.getSource().getEntity());
    }
}