package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.AvtalstypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class AvtalstypConverter extends EnumStringConverter<AvtalstypDto> {
}
