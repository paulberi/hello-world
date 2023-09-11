package se.metria.matdatabas.service.gransvarde;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.gransvarde.command.SaveGransvarde;
import se.metria.matdatabas.service.gransvarde.entity.GransvardeEntity;
import se.metria.matdatabas.service.gransvarde.exception.GransvardeNotFoundException;
import se.metria.matdatabas.service.gransvarde.exception.InactivatingGransvardeWithActiveLarmsException;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.Larmstatus;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static se.metria.matdatabas.db.tables.Gransvarde.GRANSVARDE;
import static se.metria.matdatabas.db.tables.Larmniva.LARMNIVA;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Service
public class GransvardeService {
	private GransvardeRepository repository;
	private LarmService larmService;
	private DSLContext create;

	public GransvardeService(GransvardeRepository repository, LarmService larmService, DSLContext create) {
		this.repository = repository;
		this.larmService = larmService;
		this.create = create;
	}

	private SelectJoinStep<Record> gransvardeQuery() {
		return create.select(GRANSVARDE.asterisk(), MATOBJEKT.ID.as("matobjekt_id"), MATOBJEKT.NAMN.as("matobjekt_namn"), LARMNIVA.NAMN.as("larmniva_namn"))
				.from(GRANSVARDE)
				.join(MATNINGSTYP).on(GRANSVARDE.MATNINGSTYP_ID.eq(MATNINGSTYP.ID))
				.join(MATOBJEKT).on(MATNINGSTYP.MATOBJEKT_ID.eq(MATOBJEKT.ID))
				.join(LARMNIVA).on(GRANSVARDE.LARMNIVA_ID.eq(LARMNIVA.ID));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Optional<GransvardeDto> findById(Integer id) {
		return gransvardeQuery()
				.where(GRANSVARDE.ID.eq(id))
				.fetchOptionalInto(GransvardeDto.class);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<GransvardeDto> getGransvardenListForMatningstyp(Integer matningstypId) {
		return gransvardeQuery()
				.where(GRANSVARDE.MATNINGSTYP_ID.eq(matningstypId))
				.fetchInto(GransvardeDto.class);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public GransvardeDto createGransvarde(SaveGransvarde saveGransvarde) {
		GransvardeEntity gransvarde = persist(new GransvardeEntity(saveGransvarde));
		return findById(gransvarde.getId()).orElseThrow(EntityNotFoundException::new);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public GransvardeDto updateGransvarde(Integer id, SaveGransvarde saveGransvarde) throws GransvardeNotFoundException, InactivatingGransvardeWithActiveLarmsException {
		GransvardeEntity entity = repository.findById(id).orElseThrow(GransvardeNotFoundException::new);

		if (!saveGransvarde.getAktiv() && entity.getAktiv() && !canDeactivate(id)) {
			throw new InactivatingGransvardeWithActiveLarmsException();
		}

		entity.save(saveGransvarde);
		persist(entity);
		return findById(entity.getId()).orElseThrow(EntityNotFoundException::new);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void delete(Integer id) {
		repository.deleteById(id);
 	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
 	public void deleteByMatningstypId(Integer matningstypId) {
		repository.deleteByMatningstypId(matningstypId);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Integer countByLarmnivaId(Integer id) {
		return repository.countByLarmnivaId(id);
	}

	private GransvardeEntity persist(GransvardeEntity entity) {
		return repository.saveAndFlush(entity);
	}

	public boolean canDeactivate(Integer id) {
		return larmService.countByStatusAndGransvardeId(Larmstatus.LARM, id) == 0;
	}
}
