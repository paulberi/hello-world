package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.FiberPunktersattningTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class FiberPunktersattningTypConverter extends EnumStringConverter<FiberPunktersattningTypDto> {}
