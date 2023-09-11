package se.metria.xplore.kundconfig.repository.attributeConverters;

import javax.persistence.AttributeConverter;

public abstract class EnumStringConverter<T extends Enum<T>> implements AttributeConverter<T, String> {
    @Override
    public String convertToDatabaseColumn(T item) {
        if (item == null) {
            return null;
        }

        return item.toString();
    }

    @Override
    public T convertToEntityAttribute(String s) {
        return T.valueOf(getEnumType(), s);
    }

    abstract Class<T> getEnumType();
}
