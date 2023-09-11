package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.DetaljtypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class DetaljtypConverter extends EnumStringConverter<DetaljtypDto> {}
