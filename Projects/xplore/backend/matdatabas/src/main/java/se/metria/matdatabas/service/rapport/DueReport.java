package se.metria.matdatabas.service.rapport;

import org.quartz.CronExpression;

import java.text.ParseException;
import java.time.*;
import java.util.Date;

/**
 * Represents a report that is active, past the starting date and possibly due to be sent.
 */
public class DueReport {
	private final Integer id;
	private final String name;
	private final String message;
	private final LocalDateTime lastExecution;
	private final String[] recipients;
	private final CronExpression cronExpression;

	/**
	 * @param id the id of the report
	 * @param name the name of the report
	 * @param message message to be included in report
	 * @param lastExecution when the report was last successfully sent or null if never sent
	 * @param interval the send interval for the report
	 * @param recipients where to mail the report
	 */
	public DueReport(Integer id, String name, String message, LocalDateTime lastExecution, String interval, String[] recipients) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.lastExecution = lastExecution;
		this.recipients = recipients;
		try {
			String cronExpression;
			switch (interval) {
				case Tidsintervall.DAGSVIS:
					cronExpression = "0 0 0 * * ? *";
					break;
				case Tidsintervall.VECKOVIS:
					cronExpression = "0 0 0 ? * MON *";
					break;
				case Tidsintervall.MANADSVIS:
					cronExpression = "0 0 0 1W * ? *";
					break;
				case Tidsintervall.KVARTALSVIS:
					cronExpression = "0 0 0 1W 1/3 ? *";
					break;
				case Tidsintervall.ARSVIS:
					cronExpression = "0 0 0 1W 1 ? *";
					break;
				default:
					throw new IllegalArgumentException("Invalid interval: " + interval);
			}
			this.cronExpression = new CronExpression(cronExpression);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public Integer getId() {
		return id;
	}

	public String[] getRecipients() {
		return recipients;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * Considering the configured cron expression and the last time the report was sent,
	 * should the report be sent at the time passed as a parameter?
	 */
	public boolean isDueAtTime(LocalDateTime time) {
		if (lastExecution == null) {
			return true;
		}

		Date previous = Date.from(lastExecution.atZone(ZoneId.systemDefault()).toInstant());
		Date next = cronExpression.getNextValidTimeAfter(previous);

		// Has the next time been passed compared to "now"? I.e. is the report due.
		Date now = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
		return next.before(now);
	}

	public boolean isDueNow() {
		return isDueAtTime(LocalDateTime.now());
	}
}
