package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.runtime.ScriptObject;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;
import org.winterblade.minecraft.scripting.api.IScriptObjectDeserializer;

import java.util.List;

public abstract class BaseComponentDeserializer <T, TComponent> extends BaseMirroredDeserializer {
    private final Class<TComponent> componentClass;

    protected BaseComponentDeserializer(Class<TComponent> componentClass) {
        this.componentClass = componentClass;
    }

    protected abstract T newInstance();

    @Override
    protected Object DeserializeMirror(ScriptObjectMirror mirror) {
        T output = newInstance();

        // Get our registry data...
        ComponentRegistry registry = ComponentRegistry.compileRegistryFor(new Class[]{
                componentClass}, mirror);

        List<TComponent> components = registry.getComponentsOf(componentClass);

        // Allow the actual deserializer to do its work:
        update(mirror, output, components);

        return output;
    }

    protected abstract void update(ScriptObjectMirror mirror, T output, List<TComponent> components);
}
