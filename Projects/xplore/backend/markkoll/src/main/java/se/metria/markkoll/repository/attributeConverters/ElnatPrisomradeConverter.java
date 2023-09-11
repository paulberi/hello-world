package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ElnatPrisomradeDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ElnatPrisomradeConverter extends EnumStringConverter<ElnatPrisomradeDto> {}
