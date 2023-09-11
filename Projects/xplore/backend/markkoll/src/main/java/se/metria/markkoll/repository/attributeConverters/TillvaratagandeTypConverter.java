package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.TillvaratagandeTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class TillvaratagandeTypConverter extends EnumStringConverter<TillvaratagandeTypDto> {}
