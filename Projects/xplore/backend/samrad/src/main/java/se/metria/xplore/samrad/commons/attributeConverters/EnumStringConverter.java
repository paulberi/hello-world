package se.metria.xplore.samrad.commons.attributeConverters;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

/*
Från markkolls
* **/

public abstract class EnumStringConverter <T extends Enum<T>> implements AttributeConverter<T, String> {
    @Override
    public String convertToDatabaseColumn(T item) {
        if (item == null) {
            return null;
        }

        return item.toString();
    }

    @Override
    public T convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return T.valueOf(getEnumType(), s);
    }

    private Class<T> getEnumType() {
        return (Class<T>)((ParameterizedType)getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
}
