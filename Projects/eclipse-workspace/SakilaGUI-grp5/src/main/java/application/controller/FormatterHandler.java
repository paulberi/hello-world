package application.controller;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class FormatterHandler {
    public static TextFormatter<String> getCoordinateFormatter(int max) {
        Pattern decimalPattern = Pattern.compile("^$|(-?(0?|[1-9]\\d*))(\\.\\d*)?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (decimalPattern.matcher(c.getControlNewText()).matches()
                    && parseStringToDouble(c.getControlNewText()) <= max
                    && parseStringToDouble(c.getControlNewText()) >= -max) {
                    return c;
                }
            else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);

    }

    public static TextFormatter<String> getIntegerFormatter(int min, int max) {
        Pattern decimalPattern = Pattern.compile("\\d{" + min + "," + max + "}$");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (decimalPattern.matcher(c.getControlNewText()).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);

    }

    public static TextFormatter<String> getDecimalFormatter(int wholes, int decimals) {
        Pattern decimalPattern = Pattern.compile("\\d{0," + wholes + "}(\\.\\d{0," + decimals + "})?");

        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (decimalPattern.matcher(c.getControlNewText()).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        return new TextFormatter<>(filter);

    }

    private static double parseStringToDouble(String value) {
        if (value.isEmpty() || value.equals("-")) return 1;
        else return Double.valueOf(value);
    }
}
