package se.metria.matdatabas.service.statusrapport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.Larmstatus;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.MatningstypMatobjekt;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.paminnelse.PaminnelseService;
import se.metria.matdatabas.service.paminnelse.Paminnelsetidsenhet;
import se.metria.matdatabas.service.paminnelse.dto.Paminnelse;
import se.metria.matdatabas.service.paminnelse.query.PaminnelseSearchFilter;
import se.metria.matdatabas.service.scheduler.SchedulerService;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Service
public class StatusService {

	private AnvandareService anvandareService;
	private PaminnelseService paminnelseService;
	private LarmService larmService;
	private MatningstypService matningstypService;
	private JavaMailSender javaMailSender;
	private TemplateEngine emailTemplateEngine;
	private SystemloggService systemloggService;
	private SchedulerService schedulerService;


	@Value( "${applicationUrl}" )
	private String applicationUrl;

	@Value( "${mail.from}" )
	private String mailFrom;

	public StatusService(AnvandareService anvandareService,
						 PaminnelseService paminnelseService,
						 LarmService larmService,
						 MatningstypService matningstypService,
						 TemplateEngine emailTemplateEngine,
						 JavaMailSender javaMailSender,
						 SystemloggService systemloggService,
						 ThreadPoolTaskScheduler taskScheduler,
						 SchedulerService schedulerService) {
		this.anvandareService = anvandareService;
		this.paminnelseService = paminnelseService;
		this.larmService = larmService;
		this.matningstypService = matningstypService;
		this.emailTemplateEngine = emailTemplateEngine;
		this.javaMailSender = javaMailSender;
		this.systemloggService = systemloggService;
		this.schedulerService = schedulerService;
	}

	public void skickaStatusmailAllaAnvandare() {
		List<Anvandare> anvandareList = anvandareService.getAllaAnvandare();
		anvandareList.forEach(anvandare -> {
			if (anvandare.getSkickaEpost() && !anvandare.getAnvandargrupper().isEmpty()) {
				List<Paminnelse> paminnelseList = getPaminnelseList(anvandare);
				List<MatningstypMatobjekt> matningstypMatobjektList = getMatningList(anvandare);
				List<Larm> larmList = getLarmList(anvandare);

				if (!matningstypMatobjektList.isEmpty() || !paminnelseList.isEmpty() || !larmList.isEmpty()) {
					try {
						sendMail(anvandare, paminnelseList, matningstypMatobjektList, larmList);
						systemloggService.addHandelseStatusMailSent(anvandare);
					} catch (MessagingException e) {
						systemloggService.addHandelseStatusMailFailed(anvandare);
					}
				}
			}
		});
	}

	private void sendMail(Anvandare anvandare,
						  List<Paminnelse> paminnelseList,
						  List<MatningstypMatobjekt> matningstypMatobjektList,
						  List<Larm> larmList) throws MessagingException {

		final Map<Integer, String> tidsenhetMap = new HashMap<>();
		tidsenhetMap.put((int) Paminnelsetidsenhet.HOUR, "tim");
		tidsenhetMap.put((int) Paminnelsetidsenhet.DAY, "dag");
		tidsenhetMap.put((int) Paminnelsetidsenhet.WEEK, "vecka");
		tidsenhetMap.put((int) Paminnelsetidsenhet.MONTH, "mån");
		tidsenhetMap.put((int) Paminnelsetidsenhet.YEAR, "år");

		final Context ctx = new Context(new Locale("se", "SE"));
		ctx.setVariable("larm", larmList);
		ctx.setVariable("paminnelser", paminnelseList);
		ctx.setVariable("ogranskade", matningstypMatobjektList);
		ctx.setVariable("applicationUrl", applicationUrl);
		ctx.setVariable("tidsenhetMap", tidsenhetMap);
		final String htmlContent = emailTemplateEngine.process("html/send-status.html", ctx);

		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper message;

		message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject("Larm och påminnelser i Metria Miljökoll");
		message.setFrom(mailFrom);
		message.setTo(anvandare.getEpost());
		message.setText(htmlContent, true);
		javaMailSender.send(message.getMimeMessage());
	}

	private List<Paminnelse> getPaminnelseList(Anvandare anvandare) {
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder()
				.matansvarigAnvandargruppIds(anvandare.getAnvandargrupper())
				.build();
		PaminnelseSearchFilter paminnelseSearchFilter = PaminnelseSearchFilter.builder()
				.onlyForsenade(true)
				.build();
		return paminnelseService.getMatningstypPaminnelseList(matningstypSearchFilter, paminnelseSearchFilter);
	}

	private List<MatningstypMatobjekt> getMatningList(Anvandare anvandare) {
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder()
				.matningStatus(Matningstatus.EJGRANSKAT)
				.matansvarigAnvandargruppIds(anvandare.getAnvandargrupper())
				.build();

		return matningstypService.getMatningstypMatobjektList(matningstypSearchFilter);
	}

	private List<Larm> getLarmList(Anvandare anvandare) {
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder()
				.larmStatus(Larmstatus.LARM)
				.larmTillAnvandargruppIds(anvandare.getAnvandargrupper())
				.build();
		return larmService.getLarmList(larmSearchFilter, matningstypSearchFilter);
	}
}
