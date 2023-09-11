package se.metria.matdatabas.service.matningslogg;

public interface MatningsloggHandelse {
	short RAPPORTERAT = 0;
	short IMPORTERAT = 1;
	short BERAKNING = 2;
	short FELMARKERAT = 3;
	short KORRIGERAT = 4;
	short GODKANT = 5;
	short ANDRAT = 6;
}