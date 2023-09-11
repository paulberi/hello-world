package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.AgartypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class AgartypConverter extends EnumStringConverter<AgartypDto> {}
