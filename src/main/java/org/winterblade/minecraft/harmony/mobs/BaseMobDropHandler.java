package org.winterblade.minecraft.harmony.mobs;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by Matt on 5/10/2016.
 */
class BaseMobDropHandler <T> {

    protected final List<String> what;
    protected final T[] drops;

    BaseMobDropHandler(String[] what, T[] drops) {
        this.what = Lists.newArrayList(what);
        this.drops = drops;
    }

    public boolean isMatch(String entity) {
        return what == null || what.size() <= 0 || what.contains(entity);
    }

    public T[] getDrops() {
        return drops;
    }
}
