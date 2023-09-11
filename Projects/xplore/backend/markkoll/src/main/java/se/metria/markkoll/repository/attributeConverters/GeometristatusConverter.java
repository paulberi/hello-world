package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.GeometristatusDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class GeometristatusConverter extends EnumStringConverter<GeometristatusDto> {}
