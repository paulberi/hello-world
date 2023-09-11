package se.metria.matdatabas.service.rapport;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DueReportTest {

	@Test
	void isDueEveryDay() {
		var recpts = new String[]{"test@test.com"};

		LocalDateTime lastExecution = LocalDateTime.parse("2020-12-03T10:15:30");

		DueReport report = new DueReport(1, "test", "test", lastExecution, Tidsintervall.DAGSVIS, recpts);

		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-03T23:59:30")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-03T23:59:59")));
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2020-12-04T00:00:01")));
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2020-12-05T01:15:30")));
	}

	@Test
	void isDueWeekly() {
		var recpts = new String[]{"test@test.com"};

		// Thursday at 10! - the execution was apparently delayed!
		LocalDateTime lastExecution = LocalDateTime.parse("2020-12-03T10:15:30");

		DueReport report = new DueReport(1, "test", "test", lastExecution, Tidsintervall.VECKOVIS, recpts);

		// No execution on Friday
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-04T00:01:01")));

		// No execution on Sunday
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-06T00:01:00")));

		// This should execute
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2020-12-07T00:00:01")));
	}

	@Test
	void isDueMonthly() {
		var recpts = new String[]{"test@test.com"};

		// Monday 2nd November
		LocalDateTime lastExecution = LocalDateTime.parse("2020-11-02T10:15:30");

		DueReport report = new DueReport(1, "test", "test", lastExecution, Tidsintervall.MANADSVIS, recpts);

		// No more executions during this month
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-04T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-10T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-15T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-30T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-30T23:59:59")));

		// Tuesday 2nd December
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2020-12-01T00:00:01")));
	}

	@Test
	void isDueQuarterly() {
		var recpts = new String[]{"test@test.com"};

		// Thursday 1st October
		LocalDateTime lastExecution = LocalDateTime.parse("2020-10-01T10:15:30");

		DueReport report = new DueReport(1, "test", "test", lastExecution, Tidsintervall.KVARTALSVIS, recpts);

		// No more executions during this year
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-10-04T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-10T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-15T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-31T23:59:59")));

		// Friday 1st of January
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2021-01-01T00:01:01")));
	}

	@Test
	void isDueYearly() {
		var recpts = new String[]{"test@test.com"};

		// Wednesday 1st January
		LocalDateTime lastExecution = LocalDateTime.parse("2020-10-01T10:15:30");

		DueReport report = new DueReport(1, "test", "test", lastExecution, Tidsintervall.ARSVIS, recpts);

		// No more executions during this year
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-05-04T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-11-10T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-15T10:15:01")));
		assertFalse(report.isDueAtTime(LocalDateTime.parse("2020-12-31T23:59:59")));

		// Friday 1st of January next year
		assertTrue(report.isDueAtTime(LocalDateTime.parse("2021-01-01T10:15:01")));
	}
}