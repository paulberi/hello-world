package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.HinderMarkslagDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class HinderMarkslagConverter extends EnumStringConverter<HinderMarkslagDto> {}
