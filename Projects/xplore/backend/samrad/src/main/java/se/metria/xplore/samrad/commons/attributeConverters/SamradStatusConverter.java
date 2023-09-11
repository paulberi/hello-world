package se.metria.xplore.samrad.commons.attributeConverters;

import se.metria.xplore.samrad.openapi.model.SamradStatusDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SamradStatusConverter extends EnumStringConverter<SamradStatusDto> {
}
