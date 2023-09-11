package se.metria.markkoll.util;

import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtil {
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.equals("");
    }

    // Finns set.of() i Java-biblioteket, men den skapar enbart UnmodifiableSet-instanser...
    public static <T> Set<T> asSet(T... obj) {
        return new HashSet<>(Arrays.asList(obj));
    }

    public static <T> List<T>
    modelMapperList(Collection<?> source, ModelMapper modelMapper, Class<T> targetClass) {
        return source.stream().map(s -> modelMapper.map(s, targetClass)).collect(Collectors.toList());
    }

    public static <T> Optional<T> find(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).findAny();
    }

    public static <T> void emptyCollection(Collection<T> collection) {
        collection.removeAll(collection);
    }
}
