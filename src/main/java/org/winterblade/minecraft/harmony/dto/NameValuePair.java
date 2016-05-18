package org.winterblade.minecraft.harmony.dto;

/**
 * Created by Matt on 5/16/2016.
 */
public class NameValuePair <T> {
    private String name;
    private T value;

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
