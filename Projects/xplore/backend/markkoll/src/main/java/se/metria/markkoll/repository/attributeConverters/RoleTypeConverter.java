package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.RoleTypeDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter extends EnumStringConverter<RoleTypeDto> {}
