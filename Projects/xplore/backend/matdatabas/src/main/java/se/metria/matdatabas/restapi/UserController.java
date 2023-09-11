package se.metria.matdatabas.restapi;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.UserApi;
import se.metria.matdatabas.openapi.model.CountDto;
import se.metria.matdatabas.openapi.model.UserDetailsDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.paminnelse.PaminnelseService;

import javax.annotation.security.RolesAllowed;

@RequestMapping(value = "/api")
@RestController
public class UserController implements UserApi {

	private MatdatabasUser matdatabasUser;
	private LarmService larmService;
	private MatobjektService matobjektService;
	private PaminnelseService paminnelseService;

	public UserController(MatdatabasUser matdatabasUser,
						  LarmService larmService,
						  MatobjektService matobjektService,
						  PaminnelseService paminnelseService) {
		this.matdatabasUser = matdatabasUser;
		this.larmService = larmService;
		this.matobjektService = matobjektService;
		this.paminnelseService = paminnelseService;
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<UserDetailsDto> userDetails() {
		UserDetailsDto response = new UserDetailsDto()
				.username(matdatabasUser.getUsername())
				.fullname(matdatabasUser.getFullname())
				.defaultKartlagerId(matdatabasUser.getDefaultKartlagerId())
				.company(matdatabasUser.getCompany());
		for (GrantedAuthority a : matdatabasUser.getAuthorities())	{
			response.addAuthoritiesItem(a.getAuthority());
		}
		return ResponseEntity.ok(response);
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<CountDto> userLarmGet() {
		return ResponseEntity.ok(new CountDto().count(larmService.getLarmCountForAnvandare(matdatabasUser.getId())));
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<CountDto> userMatvardenOgranskadeGet() {
		return ResponseEntity.ok(new CountDto().count(matobjektService.getOgranskadeMatningarCountForAnvandare(matdatabasUser.getId())));
	}

	@Override
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ResponseEntity<CountDto> userPaminnelserGet() {
		return ResponseEntity.ok(new CountDto().count(paminnelseService.getForsenadeMatningarCountForAnvandare(matdatabasUser.getId())));
	}
}
