package se.metria.matdatabas.restapi.export.pivot;

public interface CsvPivotRecord {
	Object groupByKey();
	Object id();
	String empty();
	String values();
	String columns();
}
