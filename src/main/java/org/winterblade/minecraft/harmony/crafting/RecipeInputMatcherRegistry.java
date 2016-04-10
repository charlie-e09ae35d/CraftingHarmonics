package org.winterblade.minecraft.harmony.crafting;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.winterblade.minecraft.harmony.api.IRecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.Priority;
import org.winterblade.minecraft.harmony.api.RecipeInputMatcher;
import org.winterblade.minecraft.harmony.api.RecipeInputMatcherParameter;
import org.winterblade.minecraft.harmony.scripting.ScriptObjectReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * Created by Matt on 4/10/2016.
 */
public class RecipeInputMatcherRegistry {
    private static List<RecipeInputMatcherRegistration> inputMatcherRegistrations = new ArrayList<>();

    private RecipeInputMatcherRegistry() {}

    public static void RegisterRecipeInputMatchers(Map<ArrayList<String>, Class<IRecipeInputMatcher>> matchers) {
        for(Map.Entry<ArrayList<String>, Class<IRecipeInputMatcher>> matcher : matchers.entrySet()) {
            // If we haven't defined properties, then we don't need to register it.
            if(matcher.getKey().size() <= 0) continue;

            Class matcherClass = matcher.getValue();

            // Pull out our priority...
            RecipeInputMatcher annotation = (RecipeInputMatcher) matcherClass.getAnnotation(RecipeInputMatcher.class);
            Priority priority = annotation.priority();

            // Find what constructors we have available to us:
            Constructor[] constructors = matcherClass.getConstructors();

            if(constructors.length <= 0) {
                System.err.println("Unable to find a constructor for '" + matcherClass.getSimpleName() +
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
            Map<String, RecipeInputMatcherProperty> propertyMap = new HashMap<>();

            for(Map.Entry<String, Class<?>> prop : propertyTypes.entrySet()) {
                propertyMap.put(prop.getKey(),
                        new RecipeInputMatcherProperty(prop.getValue(),
                            propertyConstructorCount.get(prop.getKey()) >= constructors.length));
            }

            inputMatcherRegistrations.add(new RecipeInputMatcherRegistration(matcherClass, priority, propertyMap, constructorMap));
        }
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
            RecipeInputMatcherParameter paramAnnotation = parameter.getAnnotation(RecipeInputMatcherParameter.class);

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
     * Get the matchers that can be created from the given object
     * @param mirror    The object to check
     * @return          The matchers that could be created.
     */
    public static List<IRecipeInputMatcher> GetMatchersFrom(ScriptObjectMirror mirror) {
        List<String> keys = Arrays.asList(mirror.getOwnKeys(true));
        List<IRecipeInputMatcher> matchers = new ArrayList<>();

        for(RecipeInputMatcherRegistration reg : inputMatcherRegistrations) {
            RegistrationCheckResultData resultData = reg.checkKeys(keys);

            if(resultData.result == RegistrationCheckResult.NO_KEYS) continue;

            Class matcherClass = reg.getMatcherClass();
            if(resultData.result == RegistrationCheckResult.MISSING_REQUIRED_KEYS) {
                System.err.println("The '" + matcherClass.getSimpleName() + "' matcher was missing the following: ");
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

            IRecipeInputMatcher matcher;
            try {
                matcher = (IRecipeInputMatcher) constuctor.newInstance(values);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Error calling constructor for '" + matcherClass.getSimpleName() + "': " + e.getMessage());
                continue;
            }

            matchers.add(matcher);
        }
        // Stuff like NBTTagCompounds will be ScriptObjectMirrors, while other things will be primatives.

        return matchers;
    }

    /**
     * Contains the registration for a given recipe input matcher
     */
    private static class RecipeInputMatcherRegistration {
        private final Class matcherClass;
        private final Priority priority;
        private final Map<String, RecipeInputMatcherProperty> propertyMap;
        private final Map<Constructor, List<String>> constructorMap;

        public RecipeInputMatcherRegistration(Class matcherClass, Priority priority,
                                              Map<String, RecipeInputMatcherProperty> propertyMap,
                                              Map<Constructor, List<String>> constructorMap) {
            this.matcherClass = matcherClass;
            this.priority = priority;
            this.propertyMap = propertyMap;
            this.constructorMap = constructorMap;
        }

        /**
         * Checks to see if the given set of keys is capable of constructing this matcher.
         * @param keys  The keys provided.
         * @return      The RegistrationCheckResultData indicating what happened.
         */
        public RegistrationCheckResultData checkKeys(List<String> keys) {
            List<String> foundKeys = new ArrayList<>();
            List<String> missingReqKeys = new ArrayList<>();

            for(Map.Entry<String, RecipeInputMatcherProperty> prop : propertyMap.entrySet()) {
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

        public Map.Entry<Constructor, List<String>> getConstructorFor(List<String> keys) {
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

        public Class getMatcherClass() {
            return matcherClass;
        }
    }

    /**
     * An individual property we want to provide.
     */
    private static class RecipeInputMatcherProperty {
        private final Class<?> type;
        private final boolean required;

        public RecipeInputMatcherProperty(Class<?> type, boolean required) {
            this.type = type;
            this.required = required;
        }

        public Class<?> getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }
    }

    private static class RegistrationCheckResultData {
        private RegistrationCheckResult result;
        private List<String> keys;

        public static RegistrationCheckResultData NoKeys = new RegistrationCheckResultData(RegistrationCheckResult.NO_KEYS, new ArrayList<>());

        public RegistrationCheckResultData(RegistrationCheckResult result, List<String> keys) {
            this.result = result;
            this.keys = keys;
        }
    }

    private static enum RegistrationCheckResult {
        NO_KEYS, MISSING_REQUIRED_KEYS, SUCCESS
    }
}
