package se.metria.markkoll.repository.attributeConverters;

import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class FastighetsforteckningAnledningConverter extends EnumStringConverter<FastighetsforteckningAnledning> {
}
