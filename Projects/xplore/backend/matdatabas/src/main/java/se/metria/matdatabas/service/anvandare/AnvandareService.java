package se.metria.matdatabas.service.anvandare;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandare.dto.EditAnvandare;
import se.metria.matdatabas.service.anvandare.model.AnvandareEntity;
import se.metria.matdatabas.service.anvandare.exception.*;
import se.metria.matdatabas.service.systemlogg.SystemloggService;
import se.metria.xplore.keycloak.service.KeyCloakService;

import javax.annotation.security.RolesAllowed;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AnvandareService {
	private AnvandareRepository repository;
	private SystemloggService systemloggService;
	private KeyCloakService keyCloakService;
	private JavaMailSender javaMailSender;
	private TemplateEngine emailTemplateEngine;

	@Value( "${applicationUrl}" )
	private String applicationUrl;

	@Value( "${mail.from}" )
	private String mailFrom;

	@Value("${realm.role}")
	private String realmRole;

	public AnvandareService(AnvandareRepository repository,
							SystemloggService systemloggService,
							KeyCloakService keyCloakService,
							JavaMailSender javaMailSender,
							TemplateEngine emailTemplateEngine) {
		this.repository = repository;
		this.systemloggService = systemloggService;
		this.keyCloakService = keyCloakService;
		this.javaMailSender = javaMailSender;
		this.emailTemplateEngine = emailTemplateEngine;
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Page<Anvandare> getAnvandare(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection, Boolean visaInaktiva) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		if (visaInaktiva) {
			return repository.findAll(pageRequest).map(Anvandare::fromEntity);
		} else {
			return repository.findAllByAktivTrue(pageRequest).map(Anvandare::fromEntity);
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Page<Anvandare> getAnvandareForAnvandargrupp(Integer anvandargruppId, Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		return repository.findAllByAnvandargrupperId(anvandargruppId, pageRequest).map(Anvandare::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Integer getAnvandareCountForAnvandargrupp(Integer anvandargruppId) {
		return repository.countByAnvandargrupperId(anvandargruppId);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Anvandare getAnvandare(Integer id) throws AnvandareNotFoundException {
		return Anvandare.fromEntity(findAnvandare(id));
	}

	@Transactional
	public List<Anvandare> getAllaAnvandare() {
		List<AnvandareEntity> anvandareEntities = repository.findAll();
		return anvandareEntities.stream().map(Anvandare::fromEntity).collect(Collectors.toList());
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Anvandare createAnvandare(EditAnvandare editAnvandare) throws AnvandarnamnConflictException, MessagingException {
		if (exists(editAnvandare.getInloggningsnamn())) {
			throw new AnvandarnamnConflictException();
		}

		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		String password = RandomStringUtils.random( 15, characters );

		String namn = editAnvandare.getNamn();

		String lastName = StringUtils.substringAfterLast(namn," ");
		String firstName = StringUtils.substringBeforeLast(namn, " ");

		KeyCloakService.CreateUserResult createUserStatus = keyCloakService.createUser(editAnvandare.getInloggningsnamn(), firstName, lastName, password);

		Anvandare anvandare = Anvandare.fromEntity(persist(editAnvandare.toEntity()));

		if (realmRole != null && !realmRole.isEmpty()) {
			keyCloakService.addRealmRoleToUser(anvandare.getInloggningsnamn(), realmRole);
		}

		switch (createUserStatus) {
			case USER_CREATED:
				sendNewUserEmail(anvandare, password);
				break;

			case USER_EXISTS:
				sendNewUserEmail(anvandare, null);
				break;
		}

		systemloggService.addHandelseUserCreated(anvandare);

		return anvandare;
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Anvandare updateAnvandare(Integer id, EditAnvandare editAnvandare) throws AnvandareNotFoundException {
		AnvandareEntity entity = findAnvandare(id);
		Anvandare before = Anvandare.fromEntity(entity);

		if (before.getInloggningsnamn() != null && editAnvandare.getInloggningsnamn() != null && before.getInloggningsnamn().equals(editAnvandare.getInloggningsnamn())) {
			Anvandare anvandareAfter = Anvandare.fromEntity(persist(editAnvandare.copyToEntity(entity)));
			systemloggService.addHandelseUserModified(before, anvandareAfter);

			return anvandareAfter;
		} else {
			throw new IllegalArgumentException("Inloggningsnamn får inte ändras");
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public boolean exists(String inloggningsnamn) {
		return repository.existsByInloggningsnamn(inloggningsnamn);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteAnvandare(Integer id) throws AnvandareNotFoundException, AnvandareHasLoggedInException {
		Anvandare anvandare = Anvandare.fromEntity(findAnvandare(id));

		if (anvandare.getInloggadSenast() != null) {
			throw new AnvandareHasLoggedInException();
		}

		if (realmRole != null && !realmRole.isEmpty()) {
			keyCloakService.removeRealmRoleFromUser(anvandare.getInloggningsnamn(), realmRole);
		}

		repository.deleteById(id);

		systemloggService.addHandelseUserRemoved(anvandare);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Anvandare anonymizeAnvandare(Integer id) throws AnvandareNotFoundException {
		Anvandare anvandare = Anvandare.fromEntity(persist(findAnvandare(id).anonymize()));

		systemloggService.addHandelseUserAnonymized(anvandare);

		return anvandare;
	}

	@Transactional
	public void updateInloggadSenast(Integer id) {
		repository.updateInloggadSenast(LocalDateTime.now(), id);
	}

	private void sendNewUserEmail(Anvandare anvandare, String password) throws MessagingException {
		final Context ctx = new Context(new Locale("se", "SE"));
		ctx.setVariable("password", password);
		ctx.setVariable("anvandare", anvandare);
		ctx.setVariable("applicationUrl",applicationUrl);

		final String htmlContent = emailTemplateEngine.process("html/create-user.html", ctx);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		final MimeMessageHelper message =
				new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("Välkommen till Miljökoll");
		message.setFrom(mailFrom);
		message.setTo(anvandare.getInloggningsnamn());

		// Create the HTML body using Thymeleaf
		message.setText(htmlContent, true); // true = isHtml

		javaMailSender.send(message.getMimeMessage());
	}

	private AnvandareEntity findAnvandare(Integer id) throws AnvandareNotFoundException {
		return repository.findById(id).orElseThrow(() -> new AnvandareNotFoundException());
	}

	public Boolean anvandareWithSpecifiedDefaultLayerExist(Integer layerId) {
		return repository.existsByDefaultKartlagerId(layerId);
	}

	private AnvandareEntity persist(AnvandareEntity entity) {
		return repository.saveAndFlush(entity);
	}
}
