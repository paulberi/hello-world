package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.TillvaxtomradeDto;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class TillvaxtomradeConverter extends EnumStringConverter<TillvaxtomradeDto> {}
