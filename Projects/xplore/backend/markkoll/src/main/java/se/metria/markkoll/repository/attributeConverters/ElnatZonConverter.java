package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ElnatZonDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElnatZonConverter extends EnumStringConverter<ElnatZonDto> {}
