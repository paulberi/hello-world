package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.ProjekthandelseTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProjekthandelseTypConverter extends EnumStringConverter<ProjekthandelseTypDto>{
}
