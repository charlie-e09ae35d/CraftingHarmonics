package org.winterblade.minecraft.harmony.mobs.sheds.matchers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.winterblade.minecraft.harmony.api.Component;
import org.winterblade.minecraft.harmony.api.PrioritizedObject;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.mobs.drops.IMobDropMatcher;
import org.winterblade.minecraft.harmony.api.mobs.sheds.IMobShedMatcher;

/**
 * Created by Matt on 5/7/2016.
 */
@Component(properties = {"chance"})
@PrioritizedObject(priority = Priority.HIGHEST)
public class ChanceMatcher implements IMobShedMatcher {
    private final float chance;

    public ChanceMatcher(float chance) {
        this.chance = chance;
    }

    /**
     * Should return true if this matcher matches the given event
     *
     * @param evt The event to match
     * @return True if it should match; false otherwise
     */
    @Override
    public boolean isMatch(LivingEvent.LivingUpdateEvent evt, ItemStack drop) {
        return evt.getEntity().getEntityWorld().rand.nextDouble() < chance;
    }
}
