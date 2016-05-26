package org.winterblade.minecraft.harmony.scripting.deserializers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.scripting.ComponentRegistry;

import java.util.List;

public abstract class BaseComponentDeserializer <T, TComponent> extends BaseMirroredDeserializer {
    private final Class<TComponent> componentClass;

    protected BaseComponentDeserializer(Class<TComponent> componentClass) {
        this.componentClass = componentClass;
    }

    protected abstract T newInstance(String type);

    @Override
    protected Object DeserializeMirror(ScriptObjectMirror mirror) {
        T output = newInstance(mirror.containsKey("type") ? mirror.get("type").toString() : "");

        if(output == null) return null;

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
