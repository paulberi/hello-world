package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ProjektTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProjektTypConverter extends EnumStringConverter<ProjektTypDto> {}
