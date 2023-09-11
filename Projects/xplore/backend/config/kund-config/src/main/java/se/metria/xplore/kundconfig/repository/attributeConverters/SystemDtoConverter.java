package se.metria.xplore.kundconfig.repository.attributeConverters;

import se.metria.xplore.kundconfig.openapi.model.SystemDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SystemDtoConverter extends EnumStringConverter<SystemDto> {
    @Override
    Class<SystemDto> getEnumType() {
        return SystemDto.class;
    }
}
