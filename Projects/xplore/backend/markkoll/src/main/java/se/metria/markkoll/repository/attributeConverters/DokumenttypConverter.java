package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.DokumentTypDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class DokumenttypConverter extends EnumStringConverter<DokumentTypDto> {}
