package se.metria.matdatabas.service.larm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.larm.dto.*;
import se.metria.matdatabas.service.larm.entity.LarmEntity;
import se.metria.matdatabas.service.larm.entity.LarmnivaEntity;
import se.metria.matdatabas.service.larm.exception.LarmnivaNamnConflictException;
import se.metria.matdatabas.service.larm.exception.LarmnivaNotFoundException;
import se.metria.matdatabas.service.larm.query.LarmSearchFilter;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LarmService {
	private LarmRepository repository;
	private LarmnivaRepository larmnivaRepository;
	private AnvandargruppService anvandargruppService;
	private LarmJooqRepository larmJooqRepository;

	public LarmService(LarmRepository larmRepository,
					   LarmnivaRepository larmnivaRepository,
					   AnvandargruppService anvandargruppService,
					   LarmJooqRepository larmJooqRepository) {
		this.repository = larmRepository;
		this.larmnivaRepository = larmnivaRepository;
		this.anvandargruppService = anvandargruppService;
		this.larmJooqRepository = larmJooqRepository;
	}

	public Integer getLarmCountForAnvandare(Integer anvandarId) {
		List<Integer> anvandargrupper = anvandargruppService.getAnvandargrupperForAnvandare(anvandarId);
		return getLarmCountForAnvandargrupper(anvandargrupper);
	}

	public Integer getLarmCountForAnvandargrupp(Integer anvandargruppId) {
		return getLarmCountForAnvandargrupper(List.of(anvandargruppId));
	}

	public Integer getLarmCountForAnvandargrupper(List<Integer> anvandargrupper) {
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		larmSearchFilter.setLarmStatus(Larmstatus.LARM);
		larmSearchFilter.setLarmTillAnvandargruppIds(anvandargrupper);
		return getLarmList(larmSearchFilter, matningstypSearchFilter).size();
	}

	public Page<Larm> getLarmForAnvandargrupp(Integer anvandargruppId, Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		larmSearchFilter.setLarmStatus(Larmstatus.LARM);
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		larmSearchFilter.setLarmTillAnvandargruppIds(List.of(anvandargruppId));
		return getLarmListPage(larmSearchFilter, matningstypSearchFilter, pageRequest);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public List<Larm> getLarmForMatningstyp(Integer matningstypId) {
		LarmSearchFilter larmSearchFilter = LarmSearchFilter.builder().build();
		MatningstypSearchFilter matningstypSearchFilter = MatningstypSearchFilter.builder().build();
		matningstypSearchFilter.setIncludeIds(List.of(matningstypId));
		return getLarmList(larmSearchFilter, matningstypSearchFilter);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<Larmniva> getLarmnivaer() {
		List<LarmnivaEntity> larmNivaer = larmnivaRepository.findAll();
		return larmNivaer.stream().map(Larmniva::fromEntity).collect(Collectors.toList());
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<Larm> getLarmsForMatning(Long matningId) {
		return repository.findAllByMatningId(matningId)
				.stream()
				.map(Larm::fromEntity)
				.collect(Collectors.toList());
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Larmniva createLarmniva(SaveLarmniva saveLarmniva) throws LarmnivaNamnConflictException {
		if (exists(saveLarmniva.getNamn())) {
			throw new LarmnivaNamnConflictException();
		}
		var larmniva = larmnivaRepository.save(new LarmnivaEntity(saveLarmniva));
		return Larmniva.fromEntity(larmniva);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Larmniva updateLarmniva(Integer id, SaveLarmniva saveLarmniva) throws LarmnivaNotFoundException {
		LarmnivaEntity entity = findLarmniva(id);
		entity.save(saveLarmniva);
		return Larmniva.fromEntity(entity);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteLarmNiva(Integer id) {
		larmnivaRepository.deleteById(id);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteLarm(Long id) {
		repository.deleteById(id);
	}

	private LarmnivaEntity findLarmniva(Integer id) throws LarmnivaNotFoundException {
		return larmnivaRepository.findById(id).orElseThrow(() -> new LarmnivaNotFoundException());
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public boolean exists(String namn) {
		return larmnivaRepository.existsByNamn(namn);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Integer countByStatusAndGransvardeId(Short status, Integer gransvardeId) {
		return repository.countByStatusAndGransvardeId(status, gransvardeId);
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Integer countByGransvardeId(Integer gransvardeId) {
		return repository.countByGransvardeId(gransvardeId);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Larm createLarm(SaveLarm saveLarm) {
		var larm = repository.save(new LarmEntity(saveLarm));
		return Larm.fromEntityAndSaveLarm(larm, saveLarm);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<Larm> getLarmListPage(LarmSearchFilter larmSearchFilter,
											  MatningstypSearchFilter matningstypSearchFilter,
											  Pageable pageable
	) {
		return larmJooqRepository.larmListPaged(larmSearchFilter, matningstypSearchFilter, pageable).map(Larm::new);

	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<Larm> getLarmList(LarmSearchFilter larmSearchFilter, MatningstypSearchFilter matningstypSearchFilter) {

		return  larmJooqRepository.getLarmList(larmSearchFilter, matningstypSearchFilter).stream().map(Larm::new).collect(Collectors.toList());
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void kvittera(List<Long> larmIds, Integer UserId) {
		List<LarmEntity> okvitteradeLarm = repository.findAllById(larmIds).stream()
				.filter(l -> l.getStatus() == Larmstatus.LARM)
				.collect(Collectors.toList());
		for (LarmEntity larmEntity : okvitteradeLarm) {
			larmEntity.setStatus(Larmstatus.KVITTERAT);
			larmEntity.setKvitteradDatum(LocalDateTime.now());
			if (UserId != null) {
				larmEntity.setKvitteradAvId(UserId);
			}
		}
	}

}
