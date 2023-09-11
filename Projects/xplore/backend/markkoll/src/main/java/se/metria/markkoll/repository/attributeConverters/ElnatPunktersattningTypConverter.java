package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElnatPunktersattningTypConverter extends EnumStringConverter<ElnatPunktersattningTypDto> {}
