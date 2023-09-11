package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.BilagaTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class BilagaTypConverter extends EnumStringConverter<BilagaTypDto> {
}
