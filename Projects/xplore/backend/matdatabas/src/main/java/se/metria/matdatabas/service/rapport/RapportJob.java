package se.metria.matdatabas.service.rapport;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import se.metria.matdatabas.service.scheduler.JobService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.Jobstatus;
import se.metria.matdatabas.service.scheduler.SchedulerService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Locale;

@Component
public class RapportJob implements JobService {
	private final Logger logger = LoggerFactory.getLogger(RapportJob.class);
	private final SchedulerService schedulerService;
	private final RapportService rapportService;
	private final JavaMailSender javaMailSender;
	private final TemplateEngine emailTemplateEngine;
	private final RapportFetcher rapportFetcher;

	@Value("${mail.from}")
	private String mailFrom;

	public RapportJob(SchedulerService schedulerService,
					  RapportService rapportService,
					  RapportFetcher rapportFetcher,
					  JavaMailSender javaMailSender,
					  TemplateEngine emailTemplateEngine) {
		this.schedulerService = schedulerService;
		this.rapportService = rapportService;
		this.rapportFetcher = rapportFetcher;
		this.javaMailSender = javaMailSender;
		this.emailTemplateEngine = emailTemplateEngine;
	}

	@Override
	public void jobDone(String jobServiceTyp, Short status) {
		schedulerService.jobDone(jobServiceTyp, status);
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		logger.info("Executing " + JobServicetyper.RAPPORT);
		for (DueReport dueReport : rapportService.getDueReports()) {
			if (dueReport.isDueNow()) {
				try {
					logger.info("Creating report " + dueReport.getName());
					ByteArrayResource generatedReport = rapportFetcher.fetchReport(dueReport.getId());
					logger.info("Sending to: " + Arrays.toString(dueReport.getRecipients()));
					sendReport(dueReport.getName(), dueReport.getRecipients(), generatedReport, dueReport.getMessage());
					rapportService.updateReportStatus(dueReport.getId());
				} catch (Exception e) {
					logger.error("Could not execute job for report " + dueReport.getName(), e);
				}
			}
		}

		jobDone(JobServicetyper.RAPPORT, Jobstatus.OK);
	}

	private void sendReport(String reportName, String[] recpt, ByteArrayResource report, String reportMessage)
			throws MessagingException {
		final Context ctx = new Context(new Locale("se", "SE"));
		ctx.setVariable("reportName", reportName);

		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper message;

		message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject("Rapport från Miljökoll: " + reportName);
		message.setFrom(mailFrom);
		message.setBcc(recpt);
		message.setText(reportMessage, false);
		message.addAttachment("miljokoll.pdf", report);

		javaMailSender.send(message.getMimeMessage());
	}
}
