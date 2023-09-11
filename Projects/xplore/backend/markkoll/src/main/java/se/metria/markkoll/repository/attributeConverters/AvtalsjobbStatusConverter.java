package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.openapi.model.AvtalsjobbStatusDto;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AvtalsjobbStatusConverter extends EnumStringConverter<AvtalsjobbStatusDto> {}
