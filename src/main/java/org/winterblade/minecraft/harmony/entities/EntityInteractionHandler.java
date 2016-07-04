package org.winterblade.minecraft.harmony.entities;

import net.minecraft.entity.Entity;
import org.winterblade.minecraft.harmony.api.utility.CallbackMetadata;
import org.winterblade.minecraft.harmony.common.BaseEntityMatcherData;
import org.winterblade.minecraft.harmony.common.dto.Action;
import org.winterblade.minecraft.harmony.common.dto.NameClassPair;
import org.winterblade.minecraft.harmony.utility.ClassWalker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 7/4/2016.
 */
public class EntityInteractionHandler {
    private final List<String> what;
    private final EntityInteractionData[] interactions;
    private final boolean defaultDeny;

    public EntityInteractionHandler(String[] what, EntityInteractionData[] interactions, boolean defaultDeny) {
        this.what = Arrays.asList(what);
        this.interactions = interactions;
        this.defaultDeny = defaultDeny;
    }

    public boolean check(Entity target, Entity source) {
        CallbackMetadata data = new BaseEntityMatcherData(source);

        for (EntityInteractionData interaction : interactions) {
            // Check our interaction, firing whatever we need to:
            Action result = interaction.check(target, source, data);

            // Check to see if we should continue or not:
            if (result != Action.PASS) return result == Action.ALLOW;
        }

        return !defaultDeny;
    }

    /**
     * Checks to see if this applies to the target entity
     *
     * @param key The name/class pair representing the entity
     * @return True if it's applicable, false otherwise.
     */
    public boolean isMatch(NameClassPair key) {
        return what.contains(key.getName())
                || !Collections.disjoint(what, ClassWalker.getAllClassNamesOf(key.getClazz()));
    }
}
