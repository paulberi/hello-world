package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.IndataTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class IndataTypConverter extends EnumStringConverter<IndataTypDto> {}
