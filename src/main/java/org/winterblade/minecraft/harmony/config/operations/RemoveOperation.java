package org.winterblade.minecraft.harmony.config.operations;

/**
 * Created by Matt on 4/5/2016.
 */
public class RemoveOperation implements ConfigOperation {
    public String what;

    @Override
    public void Run() {
        System.out.println("Running removal operation: " + what);
    }
}
