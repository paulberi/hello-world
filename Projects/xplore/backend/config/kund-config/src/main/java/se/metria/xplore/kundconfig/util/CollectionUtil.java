package se.metria.xplore.kundconfig.util;

import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtil {
    public static <T> List<T>
    modelMapperList(Collection<?> source, ModelMapper modelMapper, Class<T> targetClass) {
        return source.stream()
                .map(s -> modelMapper.map(s, targetClass))
                .collect(Collectors.toList());
    }

    public static <T> List<T> objectsAsList(T... obj) {
        return Arrays.asList(obj);
    }
}
