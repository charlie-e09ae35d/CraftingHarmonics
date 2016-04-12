package org.winterblade.minecraft.harmony.crafting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.api.*;
import org.winterblade.minecraft.harmony.scripting.ScriptObjectReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Matt on 4/10/2016.
 */
public class ComponentRegistry {
    private static final Map<String, List<ComponentRegistration>> components = new HashMap<>();
    private final Map<String, List<Object>> componentImpls;

    private ComponentRegistry(Map<String, List<Object>> componentImpls) {
        this.componentImpls = componentImpls;
    }

    public static void registerComponents(Map<ArrayList<String>, Class<Object>> matchers) {
        for(Map.Entry<ArrayList<String>, Class<Object>> matcher : matchers.entrySet()) {
            // If we haven't defined properties, then we don't need to register it.
            if(matcher.getKey().size() <= 0) continue;

            Class componentClass = matcher.getValue();

            // Find what constructors we have available to us:
            Constructor[] constructors = componentClass.getConstructors();

            if(constructors.length <= 0) {
                System.err.println("Unable to find a constructor for '" + componentClass.getSimpleName() +
                        "'; it will not be registered.");
                continue;
            }

            Map<String, Integer> propertyConstructorCount = new HashMap<>();
            Map<String, Class<?>> propertyTypes = new HashMap<>();
            Map<Constructor, List<String>> constructorMap = new HashMap<>();

            // Time to do a bunch of stuff to figure out how to make the object...
            for(Constructor constructor : constructors) {
                List<String> props
                        = readParameters(matcher.getKey(), propertyConstructorCount, propertyTypes, constructor);

                // We couldn't accept this constructor...
                if(props == null || props.size() <= 0) continue;

                constructorMap.put(constructor, props);
            }

            // Now that we have all that dealt with...
            Map<String, ComponentProperty> propertyMap = new HashMap<>();

            for(Map.Entry<String, Class<?>> prop : propertyTypes.entrySet()) {
                propertyMap.put(prop.getKey(),
                        new ComponentProperty(prop.getValue(),
                            propertyConstructorCount.get(prop.getKey()) >= constructors.length));
            }

            // Figure out all potential classes for this component...
            List<Class> parents = new ArrayList<>();
            Class current = componentClass;

            do {
                // And grab everything.
                parents.add(current);
                Collections.addAll(parents, current.getInterfaces());
                current = current.getSuperclass();
            } while(current != Object.class); // We're not registering all components in a bucket.  You do not need this.

            ComponentRegistration reg = new ComponentRegistration(componentClass, propertyMap, constructorMap);

            // Now, register them all...
            for(Class parent : parents) {
                String name = parent.getName();
                if(!components.containsKey(name)) components.put(name, new ArrayList<>());
                components.get(name).add(reg);
            }
        }
    }

    /**
     * Compiles a registry for the given classes by identifying which components it can pull out of the mirror.
     * @param classes   The classes to compile for; useful if you have more than one type of component in your object.
     * @param mirror    The mirror to check
     * @return          The compiled registry, which you can query for each class.
     */
    public static ComponentRegistry compileRegistryFor(Class[] classes, ScriptObjectMirror mirror) {
        String[] classNames = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            classNames[i] = classes[i].getName();
        }

        return new ComponentRegistry(getComponentsIn(classNames, mirror));
    }

    /**
     * Get all components which extend or implement the given class
     * @param cls   The class to check
     * @param <T>   The type of the implementation
     * @return      The instantiated components.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getComponentsOf(Class<T> cls) {
        String name = cls.getName();
        return (List<T>) componentImpls.get(name);
    }

    /**
     * Get the components that can be created from the given object
     * @param matchingClasses   Which classes to check implementations for
     * @param mirror            The object to check
     * @return                  The class-to-components that could be created.
     */
    private static Map<String, List<Object>> getComponentsIn(String[] matchingClasses, ScriptObjectMirror mirror) {
        List<String> keys = Arrays.asList(mirror.getOwnKeys(true));
        Map<String, List<Object>> output = new HashMap<>();

        List<ComponentSearchData> searchDataList = new ArrayList<>();

        // Collect all the components we're trying to match for...
        for(String name : matchingClasses) {
            searchDataList.addAll(components.get(name).stream()
                    .map(c -> new ComponentSearchData(name, c)).collect(Collectors.toList()));
        }

        for(ComponentSearchData searchData : searchDataList) {
            ComponentRegistration reg = searchData.getRegistration();
            RegistrationCheckResultData resultData = reg.checkKeys(keys);

            if(resultData.result == RegistrationCheckResult.NO_KEYS) continue;

            Class componentClass = reg.getComponentClass();
            if(resultData.result == RegistrationCheckResult.MISSING_REQUIRED_KEYS) {
                System.err.println("The '" + componentClass.getSimpleName() + "' matcher was missing the following: ");
                for(String s : resultData.keys) {
                    System.err.println(" - " + s);
                }
                continue;
            }

            // Do the thing.
            Map.Entry<Constructor, List<String>> constructorEntry = reg.getConstructorFor(resultData.keys);

            if(constructorEntry == null) {
                System.err.println("No matching constructor could be found for the arguments given.");
                continue;
            }

            Constructor constuctor = constructorEntry.getKey();
            List<String> paramList = constructorEntry.getValue();
            Parameter[] params = constuctor.getParameters();

            Object[] values = new Object[paramList.size()];

            for(int i = 0; i < paramList.size(); i++) {
                values[i] = ScriptObjectReader.convertData(mirror.get(paramList.get(i)), params[i].getType());
            }

            Object component;
            try {
                component = constuctor.newInstance(values);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Error calling constructor for '" + componentClass.getSimpleName() + "': " + e.getMessage());
                continue;
            }

            String baseName = searchData.getName();

            if(!output.containsKey(baseName)) output.put(baseName, new ArrayList<>());
            output.get(searchData.getName()).add(component);
        }

        return output;
    }

    private static List<String> readParameters(List<String> properties,
                                               Map<String, Integer> propertyConstructorCount,
                                               Map<String, Class<?>> propertyTypes,
                                               Constructor constructor) {
        List<String> props = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name;

            // Check for annotations...
            ComponentParameter paramAnnotation = parameter.getAnnotation(ComponentParameter.class);

            // Figure out our name...
            if (paramAnnotation != null) {
                // If we have the annotation, use that..
                name = paramAnnotation.property();
            } else if (i < properties.size()) {
                // Assume that the constructor params match the order of properties
                name = properties.get(i);
            } else {
                // Otherwise... nope
                return null;
            }
            props.add(name);
            propertyTypes.put(name, parameter.getType());
            if (!propertyConstructorCount.containsKey(name)) {
                propertyConstructorCount.put(name, 1);
            } else {
                propertyConstructorCount.put(name, propertyConstructorCount.get(name) + 1);
            }
        }

        return props;
    }


    /**
     *  Contains data about a component search.
     */
    private static class ComponentSearchData {
        private final String name;
        private final ComponentRegistration registration;

        ComponentSearchData(String name, ComponentRegistration registration) {
            this.name = name;
            this.registration = registration;
        }

        public String getName() {
            return name;
        }

        ComponentRegistration getRegistration() {
            return registration;
        }
    }

    /**
     * Contains the registration for a given component
     */
    private static class ComponentRegistration {
        private final Class componentClass;
        private final Map<String, ComponentProperty> propertyMap;
        private final Map<Constructor, List<String>> constructorMap;

        ComponentRegistration(Class componentClass,
                              Map<String, ComponentProperty> propertyMap,
                              Map<Constructor, List<String>> constructorMap) {
            this.componentClass = componentClass;
            this.propertyMap = propertyMap;
            this.constructorMap = constructorMap;
        }

        /**
         * Checks to see if the given set of keys is capable of constructing this matcher.
         * @param keys  The keys provided.
         * @return      The RegistrationCheckResultData indicating what happened.
         */
        RegistrationCheckResultData checkKeys(List<String> keys) {
            List<String> foundKeys = new ArrayList<>();
            List<String> missingReqKeys = new ArrayList<>();

            for(Map.Entry<String, ComponentProperty> prop : propertyMap.entrySet()) {
                if(keys.contains(prop.getKey())) {
                    foundKeys.add(prop.getKey());
                } else if(prop.getValue().isRequired()) {
                    missingReqKeys.add(prop.getKey());
                }
            }

            if(foundKeys.size() <= 0) return RegistrationCheckResultData.NoKeys;

            return missingReqKeys.size() > 0
                    ? new RegistrationCheckResultData(RegistrationCheckResult.MISSING_REQUIRED_KEYS, missingReqKeys)
                    : new RegistrationCheckResultData(RegistrationCheckResult.SUCCESS, foundKeys);
        }

        Map.Entry<Constructor, List<String>> getConstructorFor(List<String> keys) {
            for(Map.Entry<Constructor, List<String>> ckv : constructorMap.entrySet()) {
                if(doesConstructorMatch(keys, ckv.getValue())) return ckv;
            }

            return null;
        }

        /**
         * Checks if the two lists contain the same elements
         * @param props     The keys to check against
         * @param params    The parameters to check against
         * @return          True if the lists match
         */
        private boolean doesConstructorMatch(List<String> props, List<String> params) {
            if(props == null || params.size() != props.size()) return false;

            // Copy and sort them...
            SortedSet<String> keySet = new TreeSet<>(props);
            SortedSet<String> paramSet = new TreeSet<>(params);
            return keySet.equals(paramSet);
        }

        Class getComponentClass() {
            return componentClass;
        }
    }

    /**
     * An individual property we want to provide.
     */
    private static class ComponentProperty {
        private final Class<?> type;
        private final boolean required;

        ComponentProperty(Class<?> type, boolean required) {
            this.type = type;
            this.required = required;
        }

        public Class<?> getType() {
            return type;
        }

        boolean isRequired() {
            return required;
        }
    }

    private static class RegistrationCheckResultData {
        private RegistrationCheckResult result;
        private List<String> keys;

        static RegistrationCheckResultData NoKeys = new RegistrationCheckResultData(RegistrationCheckResult.NO_KEYS, new ArrayList<>());

        RegistrationCheckResultData(RegistrationCheckResult result, List<String> keys) {
            this.result = result;
            this.keys = keys;
        }
    }

    private enum RegistrationCheckResult {
        NO_KEYS, MISSING_REQUIRED_KEYS, SUCCESS
    }
}
