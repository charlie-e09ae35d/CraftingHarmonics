package org.winterblade.minecraft.harmony.utility;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Matt on 7/4/2016.
 */
public class ClassWalker {
    private ClassWalker() {}

    private static final LoadingCache<Class<?>, Set<Class<?>>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build(new CacheLoader<Class<?>, Set<Class<?>>>() {
                @Override
                public Set<Class<?>> load(Class<?> key) throws Exception {
                    Set<Class<?>> output = new HashSet<>();
                    Queue<Class<?>> openList = new LinkedList<>();

                    openList.add(key);

                    do {
                        // Get our next item and make sure we should process it:
                        Class<?> cur = openList.poll();
                        if(output.contains(cur)) continue;
                        output.add(cur);

                        // Add our superclass, if it's not Object:
                        Class<?> superclass = cur.getSuperclass();
                        if(superclass != Object.class) openList.add(superclass);

                        // And all the interfaces...
                        Collections.addAll(openList, cur.getInterfaces());
                    } while (0 < openList.size());

                    return output;
                }
            });

    /**
     * Gets all parent classes/interfaces of the given target class.
     * @param target    The target to get
     * @return          The classes of the target
     */
    public static Set<Class<?>> getAllClassesOf(Class<?> target) {
        try {
            return cache.get(target);
        } catch (ExecutionException e) {
            return new HashSet<>();
        }
    }

    /**
     * Gets the full name of all parent classes of the given target class.
     * @param target    The target to get
     * @return          The class names of the target
     */
    public static Set<String> getAllClassNamesOf(Class<?> target) {
        return getAllClassesOf(target).stream().map(Class::getName).collect(Collectors.toSet());
    }
}