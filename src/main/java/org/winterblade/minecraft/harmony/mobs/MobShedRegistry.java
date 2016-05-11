package org.winterblade.minecraft.harmony.mobs;

import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Matt on 5/10/2016.
 */
public class MobShedRegistry {
    private MobShedRegistry() {}

    public static void handleSheds(LivingEvent.LivingUpdateEvent evt) {
        evt.getEntityLiving();
    }
}
