package se.metria.matdatabas.service.kartlager;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.db.tables.records.KartlagerFilRecord;
import se.metria.matdatabas.db.tables.records.KartlagerRecord;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.kartlager.command.SaveKartlager;
import se.metria.matdatabas.service.kartlager.dto.Kartlager;
import se.metria.matdatabas.service.kartlager.dto.Kartlagerfil;
import se.metria.matdatabas.service.kartlager.dto.Kartlagergrupp;
import se.metria.matdatabas.service.kartlager.dto.Ordered;
import se.metria.matdatabas.service.kartlager.entity.KartlagerEntity;
import se.metria.matdatabas.service.kartlager.entity.KartlagerfilEntity;
import se.metria.matdatabas.service.kartlager.exception.KartlagerInUseException;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.max;
import static se.metria.matdatabas.db.tables.Kartlager.KARTLAGER;
import static se.metria.matdatabas.db.tables.KartlagerFil.KARTLAGER_FIL;
import static se.metria.matdatabas.db.tables.KartlagerFilStil.KARTLAGER_FIL_STIL;

@Service
public class KartlagerService {
	private KartlagerRepository repository;
	private KartlagerfilRepository kartlagerfilRepository;
	private DSLContext create;
	private final AnvandareService anvandareService;


	public KartlagerService(KartlagerRepository repository,
							KartlagerfilRepository kartlagerfilRepository,
							AnvandareService anvandareService,
							DSLContext create) {
		this.repository = repository;
		this.kartlagerfilRepository = kartlagerfilRepository;
		this.anvandareService = anvandareService;
		this.create = create;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<Kartlager> findAll() {
		return repository.findAllByOrderByOrdningDesc().stream()
				.map(entity -> Kartlager.fromEntity(entity, true))
				.collect(Collectors.toList());
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Optional<Kartlager> findById(Integer id) {
		return repository.findById(id).map(entity -> Kartlager.fromEntity(entity, false));
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Optional<Kartlagerfil> getKartlagerfil(UUID kartlagerfilId) {
		return kartlagerfilRepository.findById(kartlagerfilId).map(Kartlagerfil::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public String getKartlagerfilData(UUID kartlagerfilId) {
		return kartlagerfilRepository.findById(kartlagerfilId)
				.map(KartlagerfilEntity::getFil).orElseThrow(EntityNotFoundException::new);
	}

	/**
	 * Change the order (z index) of the list of layers.
	 * The order will be interpreted as the highest z-index first.
	 */
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	@Transactional
	public void changeOrder(List<Integer> kartlagerIds) {
		for (int i = 0; i < kartlagerIds.size(); i++) {
			Optional<KartlagerEntity> maybeKartlager = repository.findById(kartlagerIds.get(i));
			if (maybeKartlager.isPresent()) {
				var kartlager = maybeKartlager.get();
				if (kartlager.getAndringsbar()) {
					kartlager.setOrdning((short) (kartlagerIds.size() - i));
				}
			}
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Kartlager save(SaveKartlager newKartlager) {
		int nextOrder = getNextOrder();

		KartlagerEntity entity = new KartlagerEntity(
				newKartlager.getNamn(),
				newKartlager.getGrupp(),
				newKartlager.getVisa(),
				newKartlager.getBeskrivning(),
				(short) nextOrder);

		for (var fil : newKartlager.getKartlagerfiler()) {
			entity.addKartlagerfil(new KartlagerfilEntity(fil.getFilnamn(), fil.getFil(), fil.getStil()));
		}

		return Kartlager.fromEntity(repository.saveAndFlush(entity), false);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Kartlager update(Integer id, SaveKartlager update) {
		KartlagerEntity entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);

		entity.setNamn(update.getNamn());
		entity.setGrupp(update.getGrupp());
		entity.setVisa(update.getVisa());
		entity.setBeskrivning(update.getBeskrivning());

		// Update sub objects
		entity.getKartlagerfiler().forEach(persisted -> {
			var updated = update.getKartlagerfiler().stream().filter(f -> persisted.getId().equals(f.getId())).findFirst();
			updated.ifPresent(u -> {
				persisted.setStil(u.getStil());
			});
		});

		// Detect deleted sub objects and remove them.
		entity.getKartlagerfiler().removeIf(persisted -> update.getKartlagerfiler().stream()
				.noneMatch(fil -> persisted.getId().equals(fil.getId())));

		// Add new sub objects.
		update.getKartlagerfiler().stream()
				.filter(fil -> fil.getId() == null)
				.forEach(fil -> entity.addKartlagerfil(new KartlagerfilEntity(fil.getFilnamn(), fil.getFil(), fil.getStil())));

		return Kartlager.fromEntity(repository.saveAndFlush(entity), false);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void delete(Integer id) throws KartlagerInUseException {
		repository.findById(id).ifPresent(kartlager -> {
			if (kartlager.getAndringsbar()) {
				if (!usedByAnvandare(id)) {
					repository.deleteById(id);
				} else {
					throw new KartlagerInUseException();
				}
			}
		});
	}

	/**
	 * When persisting new we need to set "ordning" to something. This method is used
	 * to find the maximum + 1 value.
	 */
	private int getNextOrder() {
		return create.select(coalesce(max(KARTLAGER.ORDNING), (short) 1))
				.from(KARTLAGER)
				.where(KARTLAGER.ANDRINGSBAR.isTrue())
				.fetchOne()
				.value1() + 1;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<String> getStyles() {
		return create.selectFrom(KARTLAGER_FIL_STIL).fetch().getValues(KARTLAGER_FIL_STIL.STIL);
	}

	/**
	 * Gets a list consisting of Kartlager and Kartlagergrupp elements. Kartlager with the same group property are
	 * grouped into the same Kartlagergrupp.
	 *
	 * Convenient for a client to use construct a layer tree.
	 */
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public List<Ordered> getLayerTree() {
		Map<String, Result<KartlagerRecord>> kartlagerGrouped = create.selectFrom(KARTLAGER).fetchGroups(KARTLAGER.GRUPP);
		Map<Integer, Result<KartlagerFilRecord>> filerGrouped = create.select(KARTLAGER_FIL.ID, KARTLAGER_FIL.KARTLAGER_ID, KARTLAGER_FIL.FILNAMN, KARTLAGER_FIL.STIL).from(KARTLAGER_FIL).fetch().into(KARTLAGER_FIL).intoGroups(KARTLAGER_FIL.KARTLAGER_ID);

		List<Ordered> tree = new ArrayList<>();

		for (String group : kartlagerGrouped.keySet()) {
			List<Kartlager> inGroup = kartlagerGrouped.get(group).stream().map(kartlager -> {
				Kartlager k = kartlager.into(Kartlager.class);
				if (filerGrouped.containsKey(k.getId())) {
					List<Kartlagerfil> f = filerGrouped.get(k.getId()).stream().map(kartlagerFilRecord -> kartlagerFilRecord.into(Kartlagerfil.class)).collect(Collectors.toList());
					k.setKartlagerfiler(f);
				}
				return k;
			}).collect(Collectors.toList());

			if (group == null) {
				tree.addAll(inGroup);
			} else {
				inGroup.sort(Comparator.comparing(Ordered::getOrder).reversed());
				tree.add(new Kartlagergrupp(group, inGroup));
			}
		}

		tree.sort(Comparator.comparing(Ordered::getOrder).reversed());

		return tree;
	}

	public Boolean usedByAnvandare(Integer layerId) {
		return anvandareService.anvandareWithSpecifiedDefaultLayerExist(layerId);
	}
}
