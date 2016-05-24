package org.winterblade.minecraft.harmony.common.drops;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 5/10/2016.
 */
public class BaseDropHandler<T> {
    protected final List<String> what;
    protected final List<T> drops;

    public BaseDropHandler(String[] what, T[] drops) {
        this.what = Lists.newArrayList(what);
        this.drops = drops != null ? Lists.newArrayList(drops) : new ArrayList<>();
    }

    public boolean isMatch(String entity) {
        return what == null || what.size() <= 0 || what.contains(entity);
    }

    public List<T> getDrops() {
        return drops;
    }
}
