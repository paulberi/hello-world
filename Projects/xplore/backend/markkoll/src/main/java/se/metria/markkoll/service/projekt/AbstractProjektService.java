package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.openapi.model.ManuellFastighethandelseTypDto;
import se.metria.markkoll.openapi.model.ProjektInfoDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.FastighetsforteckningService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.vardering.VarderingsprotokollService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractProjektService<Entity extends ProjektEntity, DTO, VPDTO> implements AvtalUpdater {
	@NonNull
	protected final AvtalService avtalService;

	@NonNull
	protected final AvtalRepository avtalRepository;

	@NonNull
	protected final FastighetsforteckningService fastighetsforteckningService;

	@NonNull
	private final GeometristatusService geometristatusService;

	@NonNull
	protected final LoggService loggService;

	@NonNull
	private final OmradesintrangRepository omradesintrangRepository;

	@NonNull
	protected final VarderingsprotokollService<VPDTO> varderingsprotokollService;

	@NonNull
	protected final ProjektRepository projektRepository;


	public abstract Boolean shouldHaveVarderingsprotokoll(UUID projektId);

	protected abstract Optional<Entity> getProjektEntity(UUID projektId);
	protected abstract DTO createProjektDto(Entity entity);

	@Override
	@Transactional
	public UUID addManuelltAvtal(UUID projektId, UUID fastighetId) {
		var avtalId = avtalService.create(projektId, fastighetId);

		fastighetsforteckningService.create(projektId, fastighetId, avtalId,
			FastighetsforteckningAnledning.MANUELLT_TILLAGD);

		if (shouldHaveVarderingsprotokoll(projektId)) {
			var vp = varderingsprotokollService.getEmptyVarderingsprotokoll();
			varderingsprotokollService.create(vp, avtalId);
		}

		loggService.addManuellFastighetHandelse(projektId, fastighetId, ManuellFastighethandelseTypDto.TILLAGD);

		return avtalId;
	}

	@Override
	@Transactional
	public List<UUID> addImportAvtal(UUID projektId, List<UUID> fastighetIds, GeometristatusDto geometristatusDto) {
		log.info("Skapar avtal för {} fastigheter...", fastighetIds.size());

		var avtalIds = avtalService.create(projektId, fastighetIds);
		fastighetsforteckningService.create(projektId, avtalIds, FastighetsforteckningAnledning.IMPORTVERSION);

		if (shouldHaveVarderingsprotokoll(projektId)) {
			log.info("Skapar klassificerade värderingsprotokoll...");
			var currentVersionId = projektRepository.getCurrentVersionId(projektId);
			varderingsprotokollService.createKlassificeradeVarderingsprotokoll(currentVersionId, avtalIds);
		}

		log.info("Sätter geometristatus till {}...", geometristatusDto);
		geometristatusService.createGeometristatus(projektId, avtalIds, geometristatusDto);

		return avtalIds;
	}

	@Override
	@Transactional
	public void updateAvtal(UUID projektId, Collection<UUID> fastighetIds, GeometristatusDto geometristatusDto) {
		log.info("Uppdaterar avtal för {} fastigheter...", fastighetIds.size());

		var avtalList = avtalService.getAvtal(projektId, fastighetIds);
		var avtalIds = avtalList.stream().map(av -> av.getId()).collect(Collectors.toList());
		geometristatusService.createGeometristatus(projektId, avtalIds, geometristatusDto);

		if (shouldHaveVarderingsprotokoll(projektId) && geometristatusDto == GeometristatusDto.UPPDATERAD) {
			log.info("Uppdaterar klassificerade värderingsprotokoll...");
			var currentVersionId = projektRepository.getCurrentVersionId(projektId);
			varderingsprotokollService.updateKlassificeradeVarderingsprotokoll(currentVersionId, avtalIds);
		}

		fastighetsforteckningService.setExcluded(projektId, fastighetIds, false);
	}

	@Override
	@Transactional
	public void
	updateVarderingsprotokoll(UUID projektId, Collection<UUID> fastighetIds) {

		if (!fastighetIds.isEmpty() && shouldHaveVarderingsprotokoll(projektId)) {
			var currentVersionId = projektRepository.getCurrentVersionId(projektId);
			var avtalIds = avtalRepository.findAllIdsByProjektIdAndFastighetIdIn(projektId, fastighetIds);
			varderingsprotokollService.updateKlassificeradeVarderingsprotokoll(currentVersionId, avtalIds);
		}
	}

	@Override
	@Transactional
	public void updateAvtal(UUID projektId, UUID fastighetId) {
		if (shouldHaveVarderingsprotokoll(projektId) &&
			geometristatusService.getGeometristatus(projektId, fastighetId) == GeometristatusDto.UPPDATERAD)
		{
			var avtal = avtalService.getAvtal(projektId, fastighetId);
			var currentVersionId = projektRepository.getCurrentVersionId(projektId);
			var vp = varderingsprotokollService.getKlassificeratVarderingsprotokoll(avtal.getId(),
				currentVersionId);
			varderingsprotokollService.update(vp, avtal.getId());
		}

		fastighetsforteckningService.setExcluded(projektId, fastighetId, false);
	}

	public Optional<ProjektInfoDto> getProjektInfo(UUID projektId) {
		return getProjektEntity(projektId).map(this::createProjektInfoDto);
	}

	public Optional<DTO> getProjektDto(UUID projektId) {
		return getProjektEntity(projektId).map(this::createProjektDto);
	}


	protected void mapProjektInfo(ProjektInfoDto projektInfo, Entity projektEntity) {
		if (projektInfo.getId() != null) {
			projektEntity.setId(projektInfo.getId());
		}

		projektEntity.setNamn(projektInfo.getNamn());
		projektEntity.setOrt(projektInfo.getOrt());
		projektEntity.setProjekttyp(projektInfo.getProjektTyp());
		projektEntity.setBeskrivning(projektInfo.getBeskrivning());
		projektEntity.setStartDatum(projektInfo.getStartDatum());
		projektEntity.setUtskicksstrategi(projektInfo.getUtskicksstrategi());
		projektEntity.setUppdragsnummer(projektInfo.getUppdragsnummer());
		projektEntity.setProjektnummer(projektInfo.getProjektnummer());
		projektEntity.setUtbetalningskonto(projektInfo.getUtbetalningskonto());
		projektEntity.setAnsvarigProjektledare(projektInfo.getAnsvarigProjektledare());
		projektEntity.setAnsvarigKonstruktor(projektInfo.getAnsvarigKonstruktor());
	}

	protected ProjektInfoDto createProjektInfoDto(Entity projektEntity) {
		return new ProjektInfoDto()
				.id(projektEntity.getId())
				.namn(projektEntity.getNamn())
				.projektTyp(projektEntity.getProjekttyp())
				.ort(projektEntity.getOrt())
				.startDatum(projektEntity.getStartDatum())
				.beskrivning(projektEntity.getBeskrivning())
				.utskicksstrategi(projektEntity.getUtskicksstrategi())
				.uppdragsnummer(projektEntity.getUppdragsnummer())
			    .kundId(projektEntity.getKundId())
				.projektnummer(projektEntity.getProjektnummer())
				.utbetalningskonto(projektEntity.getUtbetalningskonto())
				.ansvarigProjektledare(projektEntity.getAnsvarigProjektledare())
				.ansvarigKonstruktor(projektEntity.getAnsvarigKonstruktor());
	}
}
