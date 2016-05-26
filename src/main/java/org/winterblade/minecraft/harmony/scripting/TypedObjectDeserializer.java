package org.winterblade.minecraft.harmony.scripting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraftforge.fml.common.Loader;
import org.winterblade.minecraft.harmony.api.ComponentParameter;
import org.winterblade.minecraft.harmony.api.ModDependent;
import org.winterblade.minecraft.harmony.api.TypedObject;
import org.winterblade.minecraft.harmony.common.utility.LogHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * A better deserializer that doesn't require nearly as much nonsense.
 */
public class TypedObjectDeserializer {
    private static final Map<String, Map<String, ObjectRegistration>> objectMap = new HashMap<>();

    public static void register(Map<String, Class<Object>> typedObjectClasses) {
        for (Map.Entry<String, Class<Object>> matcher : typedObjectClasses.entrySet()) {
            Class objectClass = matcher.getValue();
            ModDependent modAnnotation = (ModDependent) objectClass.getAnnotation(ModDependent.class);
            TypedObject annotation = (TypedObject) objectClass.getAnnotation(TypedObject.class);

            if (!modAnnotation.dependsOn().equals("") && !Loader.isModLoaded(modAnnotation.dependsOn())) {
                LogHelper.error("TypedObject '" + objectClass.getSimpleName() + "' depends on '"
                        + modAnnotation.dependsOn() + "'; it will not be registered.");
                continue;
            }

            // Find what constructors we have available to us:
            Constructor[] constructors = objectClass.getConstructors();

            if (constructors.length <= 0) {
                LogHelper.error("Unable to find a public constructor for '" + objectClass.getSimpleName() +
                        "'; it will not be registered.");
                continue;
            }

            Map<String, Integer> propertyConstructorCount = new HashMap<>();
            Map<String, Class<?>> propertyTypes = new HashMap<>();
            Map<Constructor, List<String>> constructorMap = new HashMap<>();

            // Time to do a bunch of stuff to figure out how to make the object...
            for (Constructor constructor : constructors) {
                List<String> props
                        = readParameters(annotation.properties(), propertyConstructorCount, propertyTypes, constructor);

                // We couldn't accept this constructor...
                if (props == null || props.size() <= 0) continue;

                constructorMap.put(constructor, props);
            }

            // TODO: Figure out properties for non-constructor arguments.
            // TODO: Figure out if property types are component arrays.

            // Now that we have all that dealt with...
            Map<String, ObjectProperty> propertyMap = new HashMap<>();

            for (Map.Entry<String, Class<?>> prop : propertyTypes.entrySet()) {
                propertyMap.put(prop.getKey(),
                        new ObjectProperty(prop.getValue(),
                                propertyConstructorCount.get(prop.getKey()) >= constructors.length));
            }

            // Figure out all potential classes for this component...
            List<Class> parents = new ArrayList<>();
            Class current = objectClass;

            do {
                Collections.addAll(parents, current.getInterfaces());
                current = current.getSuperclass();
            }
            while (current != Object.class); // We're not registering all objectMap in a bucket.  You do not need this.

            ObjectRegistration reg = new ObjectRegistration(objectClass, propertyMap, constructorMap);

            // Now, register them all...
            for (Class parent : parents) {
                String name = parent.getName();
                if (!objectMap.containsKey(name)) objectMap.put(name, new HashMap<>());
                objectMap.get(name).put(annotation.name(), reg);
            }
        }
    }

    @Nullable
    public static <T> T deserialize(Class<T> cls, ScriptObjectMirror mirror) {
        // Check if our mirror has a type:
        if(!mirror.containsKey("type") || !objectMap.containsKey(cls.getName())) return null;

        // Get the registration for it...
        Map<String, ObjectRegistration> objectRegistrations = objectMap.get(cls.getName());
        if (objectRegistrations == null) return null; // Sanity check

        // Find the right type as well
        ObjectRegistration reg = objectRegistrations.get(mirror.get("type").toString());

        // Grab all of our keys from the mirror:
        List<String> keys = Arrays.asList(mirror.getOwnKeys(true));

        // Match our available keys...
        RegistrationCheckResultData resultData = reg.checkKeys(keys);
        if (resultData.result == RegistrationCheckResult.NO_KEYS) return null;

        // Now see if we have the required keys...
        Class componentClass = reg.getComponentClass();
        if (resultData.result == RegistrationCheckResult.MISSING_REQUIRED_KEYS) {
            LogHelper.warn("The '" + componentClass.getSimpleName() + "' matcher was missing the following: ");
            for (String s : resultData.keys) {
                LogHelper.warn(" - " + s);
            }
            return null;
        }

        // Try and make the constructor.
        Map.Entry<Constructor, List<String>> constructorEntry = reg.getConstructorFor(resultData.keys);

        if (constructorEntry == null) {
            LogHelper.error("No matching constructor could be found for the arguments given.");
            return null;
        }

        Constructor constuctor = constructorEntry.getKey();
        List<String> paramList = constructorEntry.getValue();
        Parameter[] params = constuctor.getParameters();

        Object[] values = new Object[paramList.size()];

        for (int i = 0; i < paramList.size(); i++) {
            values[i] = NashornConfigProcessor.getInstance().nashorn.convertData(mirror.get(paramList.get(i)), params[i].getType());
        }


        // Construct our object
        T component;
        try {
            component = cls.cast(constuctor.newInstance(values));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LogHelper.error("Error calling constructor for '" + componentClass.getSimpleName() + "': " + e.getMessage());
            return null;
        }

        // Find out what properties we're missing
        Set<String> remainingParams = reg.propertyMap.keySet();
        remainingParams.removeAll(paramList);

        // If we don't have any, return the object as-is
        if(remainingParams.size() <= 0) return component;

        for(String paramName : remainingParams) {
            ObjectProperty prop = reg.propertyMap.get(paramName);
            Object value =  NashornConfigProcessor.getInstance().nashorn.convertData(mirror.get(paramName), prop.getType());
            // TODO: Do something with the value.
        }

        return component;
    }

    private static List<String> readParameters(String[] properties,
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
            } else if (i < properties.length) {
                // Assume that the constructor params match the order of properties
                name = properties[i];
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
     * Contains the registration for a given component
     */
    private static class ObjectRegistration {
        private final Class componentClass;
        private final Map<String, ObjectProperty> propertyMap;
        private final Map<Constructor, List<String>> constructorMap;

        ObjectRegistration(Class componentClass,
                           Map<String, ObjectProperty> propertyMap,
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

            for(Map.Entry<String, ObjectProperty> prop : propertyMap.entrySet()) {
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

        @Override
        public String toString() {
            return "ObjectRegistration{" + componentClass + '}';
        }
    }

    /**
     * An individual property we want to provide.
     */
    private static class ObjectProperty {
        private final Class<?> type;
        private final boolean required;

        ObjectProperty(Class<?> type, boolean required) {
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
