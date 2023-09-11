package se.metria.xplore.maputils;

public class CqlEscape {
    public static String cqlEscape(String str) {
        return str.replaceAll("'", "''");
    }
}
