package org.winterblade.minecraft.harmony.common.dto;

/**
 * Created by Matt on 7/4/2016.
 */
public class NameClassPair {
    private final String name;
    private final Class<?> clazz;

    private NameClassPair(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameClassPair that = (NameClassPair) o;

        return getName().equals(that.getName()) && getClazz().equals(that.getClazz());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getClazz().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + "/" + clazz.getName();
    }
}