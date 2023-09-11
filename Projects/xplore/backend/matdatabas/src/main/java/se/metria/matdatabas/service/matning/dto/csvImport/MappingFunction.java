package se.metria.matdatabas.service.matning.dto.csvImport;

@FunctionalInterface
public interface MappingFunction {
    void map(ImportMatning importMatning, String value);
}