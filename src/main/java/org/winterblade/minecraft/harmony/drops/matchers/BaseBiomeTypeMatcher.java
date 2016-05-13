package org.winterblade.minecraft.harmony.drops.matchers;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.BiomeDictionary;
import org.winterblade.minecraft.harmony.api.drops.BaseDropMatchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/11/2016.
 */
public class BaseBiomeTypeMatcher {
    private final List<BiomeDictionary.Type> types = new ArrayList<>();

    public BaseBiomeTypeMatcher(String[] biomesTags) {
        // Add all our tags:
        for(String tag : biomesTags) {
            tag = tag.toUpperCase();
            for(BiomeDictionary.Type type : BiomeDictionary.Type.values()) {
                if(!type.name().equals(tag)) continue;
                types.add(type);
                break;
            }
        }
    }

    protected BaseDropMatchResult matches(Entity entity) {
        if(entity == null) return BaseDropMatchResult.False;

        // Get all the tags for this biome:
        BiomeDictionary.Type[] biomeTags = BiomeDictionary.getTypesForBiome(
                entity.getEntityWorld().getBiomeGenForCoords(entity.getPosition()));

        // Figure out if we match:
        for(BiomeDictionary.Type type : biomeTags) {
            if(!types.contains(type)) continue;
            return BaseDropMatchResult.True;
        }

        return BaseDropMatchResult.False;
    }

}
