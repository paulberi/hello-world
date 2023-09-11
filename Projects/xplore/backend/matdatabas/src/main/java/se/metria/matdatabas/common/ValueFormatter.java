package se.metria.matdatabas.common;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import se.metria.matdatabas.service.matning.Detektionsomrade;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static se.metria.matdatabas.db.tables.Matning.MATNING;

@Component
@RequestScope
public class ValueFormatter {
	private NumberFormat coordinateFormat;
	private NumberFormat matvardeFormat;

	public ValueFormatter() {
		coordinateFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("sv"));
		coordinateFormat.setGroupingUsed(false);
		coordinateFormat.setMaximumFractionDigits(2);

		matvardeFormat = NumberFormat.getNumberInstance(Locale.forLanguageTag("sv"));
		matvardeFormat.setGroupingUsed(false);
		matvardeFormat.setMaximumFractionDigits(4);
	}

	public String formatCoordinate(BigDecimal coordinate) {
		if (coordinate != null) {
			return coordinateFormat.format(coordinate);
		}
		return "";
	}

	public String formatMatvarde(Double matvarde) {
		if (matvarde != null) {
			return matvardeFormat.format(matvarde);
		}
		return "";
	}

	public String formatDetektionsomrade(Short inomDetektionsomrade) {
		switch (inomDetektionsomrade) {
			case Detektionsomrade.UNDER:
				return "<";
			case Detektionsomrade.OVER:
				return ">";
			default:
				return "";
		}
	}

	public String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "";
		}

		return dateTime.truncatedTo(ChronoUnit.SECONDS)
				.toString()
				.replace("T", " ");
	}

	public String formatDate(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "";
		}

		return dateTime.toLocalDate().toString();
	}
}
