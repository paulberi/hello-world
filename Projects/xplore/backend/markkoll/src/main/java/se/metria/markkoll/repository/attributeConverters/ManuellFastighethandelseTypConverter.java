package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ManuellFastighethandelseTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ManuellFastighethandelseTypConverter extends EnumStringConverter<ManuellFastighethandelseTypDto> {
}
