package se.metria.matdatabas.service.matobjekt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.db.tables.records.MatrundaMatningstypRecord;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.bifogadfil.BifogadfilService;
import se.metria.matdatabas.service.bifogadfil.exception.BifogadfilNotFoundException;
import se.metria.matdatabas.service.definitionmatningstyp.StorheterConstants;
import se.metria.matdatabas.service.handelse.HandelseService;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportError;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportMatning;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matningstyp.MatningstypRepository;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matningstyp.exception.MatningstypHasMatningarException;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.matobjekt.dto.*;
import se.metria.matdatabas.service.matobjekt.entity.MatobjektEntity;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektConflictException;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatobjektService {
    private MatobjektRepository matbojektRepository;
    private MatningstypRepository matningstypRepository;
    private EntityManager entityManager;
    private AnvandargruppService anvandargruppService;
    private BifogadfilService bifogadfilService;
    private SystemloggService systemloggService;
    private MatningstypService matningstypService;
    private HandelseService handelseService;
    private MatningService matningService;
    private MatobjektJooqRepository matobjektJooqRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // RH00 offset only applicable to Stockholm
    public static final double RH00_TO_RH2000 = 0.525;

    public MatobjektService(MatobjektRepository matbojektRepository,
                            EntityManager entityManager,
                            AnvandargruppService anvandargruppService,
                            BifogadfilService bifogadfilService,
                            SystemloggService systemloggService,
                            MatningstypService matningstypService,
                            MatningstypRepository matningstypRepository,
                            HandelseService handelseService,
                            MatobjektJooqRepository matobjektJooqRepository,
                            MatningService matningService) {

        this.matbojektRepository = matbojektRepository;
        this.matningstypRepository = matningstypRepository;
        this.entityManager = entityManager;
        this.anvandargruppService = anvandargruppService;
        this.bifogadfilService = bifogadfilService;
        this.systemloggService = systemloggService;
        this.matningstypService = matningstypService;
        this.handelseService = handelseService;
        this.matningService = matningService;
        this.matobjektJooqRepository = matobjektJooqRepository;
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public Page<ListMatobjekt> getMatobjekt(MatningstypSearchFilter filter, Pageable pageable) {
        return matobjektJooqRepository.matobjektPage(filter, pageable).map(ListMatobjekt::fromRecord);
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public Matobjekt getMatobjekt(Integer id) throws MatobjektNotFoundException {
        Matobjekt matobjekt = Matobjekt.fromEntity(this.findMatobjekt(id));
        List<MatningstypMatrunda> matrundor = getMatrundor(id);

        matobjekt.setMatrundor(matrundor);
        return matobjekt;
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public Matobjekt createMatobjekt(EditMatobjekt editMatobjekt) throws MatobjektConflictException {
        if (exists(editMatobjekt.getNamn())) {
            throw new MatobjektConflictException();
        }

        Matobjekt matobjekt = Matobjekt.fromEntity(persist(editMatobjekt.toEntity()));
        matobjekt.setMatrundor(getMatrundor(matobjekt.getId()));

        systemloggService.addHandelseMatobjektCreated(matobjekt);

        return matobjekt;
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public Matobjekt updateMatobjekt(Integer id, EditMatobjekt editMatobjekt) throws MatobjektNotFoundException {
        MatobjektEntity entity = findMatobjekt(id);
        Matobjekt before = Matobjekt.fromEntity(entity);
        Matobjekt after = Matobjekt.fromEntity(persist(editMatobjekt.copyToEntity(entity)));
        after.setMatrundor(getMatrundor(after.getId()));

        purgeRemovedFiles(before, after);

        systemloggService.addHandelseMatobjektModified(before, after);

        return after;
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public void deleteMatobjekt(Integer id) throws MatobjektNotFoundException, MatningstypHasMatningarException {
        matningstypService.deleteByMatobjektId(id);
        handelseService.deleteByMatobjektId(id);

        MatobjektEntity entity = findMatobjekt(id);
        Matobjekt matobjekt = Matobjekt.fromEntity(entity);

        matbojektRepository.deleteById(id);

        purgeRemovedFiles(matobjekt, new Matobjekt());

        systemloggService.addHandelseMatobjektRemoved(matobjekt);
    }

    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public boolean canDelete(Integer id) throws MatobjektNotFoundException {
        return !matbojektRepository.hasMatningar(findMatobjekt(id).getId());
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public List<String> getMatobjektNamn(String q, Integer typ) {
        return typ != null ?
                matbojektRepository.findAllNamnLikeAndTypEquals(q, typ.shortValue()) :
                matbojektRepository.findAllNamnLike(q);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public Optional<String> getMatobjektNamn(Integer id) {
        return matbojektRepository.findNamnById(id);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public Optional<Short> getMatobjektTyp(Integer id) {
        return matbojektRepository.findTypById(id);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public List<String> getMatobjektFastigheter(String q) {
        return matbojektRepository.findAllFastighetLike(q);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public Optional<Integer> getMatobjektIdByNamn(String namn) {
        return matbojektRepository.findIdByNamnIgnoreCase(namn);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public boolean exists(String namn) {
        return matbojektRepository.existsByNamn(namn);
    }

    @RolesAllowed(MatdatabasRole.OBSERVATOR)
    public boolean exists(Integer id) {
        return matbojektRepository.existsById(id);
    }

    public Integer getOgranskadeMatningarCountForAnvandare(Integer anvandarId) {
        List<Integer> anvandargrupper = anvandargruppService.getAnvandargrupperForAnvandare(anvandarId);

        if (anvandargrupper.isEmpty()) {
            return 0;
        }

        String query = "SELECT COUNT(*) FROM aktiva_matningar where matansvarig_anvandargrupp_id IN :anvandargrupper AND status = 0";
        BigInteger count = (BigInteger) entityManager.createNativeQuery(query)
                .setParameter("anvandargrupper", anvandargrupper)
                .getSingleResult();
        return count.intValue();
    }

    @RolesAllowed(MatdatabasRole.MATRAPPORTOR)
    public List<ImportMatning> parseCsvImport(InputStream csv, ImportFormat format, Kallsystem kallsystem) throws IllegalArgumentException, IOException {
        var imports = matningService.parseCsvImport(csv, format);
        if (ImportFormat.INSTRUMENT.equals(format)) {
            validateInstrument(imports);
        } else {
            validateStandard(imports);
        }

        for (var matning : imports) {
            transformImportMatning(matning, kallsystem);
        }

        return imports;
    }

    private void transformImportMatning(ImportMatning matning, Kallsystem kallsystem) {
        if (matning.getMatningstypId() == null) {
            return; // Ingen tranformation om vi inte vet vad vi transformerar
        }
        var matningstyp = matningstypService.findById(matning.getMatningstypId());

        if (!matningstyp.isPresent()) {
            return; // Ingen tranformation om vi inte vet vad vi transformerar
        }

        parseDoubleVarde(matning.getAvlastVarde()).ifPresentOrElse(
                varde -> {
                    var storhet = matningstyp.get().getStorhet();
                    final var transformed = transformVarde(kallsystem.getNamn(), varde, storhet);
                    matning.setAvlastVarde(String.valueOf(transformed).replace(".", ","));
                },

                () -> matning.setAvlastVarde(null)
        );

        parseDoubleVarde(matning.getBeraknatVarde()).ifPresentOrElse(
                varde -> {
                    var storhet = matningstyp.get().getBeraknadStorhet();
                    final var transformed = transformVarde(kallsystem.getNamn(), varde, storhet);
                    matning.setBeraknatVarde(String.valueOf(transformed).replace(".", ","));
                },

                () -> matning.setBeraknatVarde(null)
        );
    }

    private double transformVarde(String kallsystemNamn, double varde, String storhet) {
        final var rh00_transform_storheter = new HashSet<>(Arrays.asList(new String[]{
                StorheterConstants.NIVA,
                StorheterConstants.RORELSE,
        }));

        if (kallsystemNamn.equals(StandardKallsystem.RH00Stockholm.getNamn()) &&
                storhet != null &&
                rh00_transform_storheter.contains(storhet)) {
            return varde + RH00_TO_RH2000;
        } else {
            return varde;
        }
    }


    private Optional<Double> parseDoubleVarde(String vardeString) throws NumberFormatException {
        if (vardeString == null || vardeString.equalsIgnoreCase("")) {
            return Optional.empty();
        } else {
            return Optional.of(Double.parseDouble(vardeString.replace(",", ".")));
        }
    }


    private void validateStandard(List<ImportMatning> imports) {
        Map<String, Optional<Integer>> nameMatobjektId = imports.stream()
                .map(ImportMatning::getMatobjekt)
                .distinct()
                .collect(Collectors.toMap(namn -> namn, namn -> matbojektRepository.findIdByNamnIgnoreCase(namn)));

        imports.forEach(im -> {
            nameMatobjektId.get(im.getMatobjekt()).ifPresentOrElse(
                    id -> im.setMatobjektId(id),
                    () -> im.getImportFel().add(ImportError.MATOBJEKT_MISSING)
            );
        });

        Map<Integer, List<Matningstyp>> matobjektIdMatningstyper = nameMatobjektId.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(id -> id, id -> matningstypService.getMatningstyperForMatobjekt(id)));


        for (var im : imports) {
            if (im.getMatobjektId() != null) {
                List<Matningstyp> matobjektMatningstyper = matobjektIdMatningstyper.get(im.getMatobjektId());

                Map<String, Matningstyp> matningstypMatches = new HashMap<>();

                for (var matningstyp : matobjektMatningstyper) {
                    if (matningstyp.getTyp().equalsIgnoreCase(im.getMatningstyp())) {
                        if (im.getStorhet() != null) {
                            if (matningstyp.getStorhet().equalsIgnoreCase(im.getStorhet())) {
                                matningstypMatches.put(matningstyp.getStorhet(), matningstyp);
                            }
                        } else {
                            matningstypMatches.put(matningstyp.getStorhet(), matningstyp);
                        }
                    }
                }

                if (matningstypMatches.size() == 0) {
                    im.getImportFel().add(ImportError.MATNINGSTYP_MISSING);
                } else {
                    if (matningstypMatches.size() == 1) {
                        im.setMatningstypId(matningstypMatches.values().stream().findAny().get().getId());
                    } else {
                        im.getImportFel().add(ImportError.MATNINGSTYP_STORHET_MISSING);
                    }
                }
            }
        }

        imports.forEach(im -> {
            Optional<Integer> matobjektId = nameMatobjektId.get(im.getMatobjekt());

            Optional<Matningstyp> optMatningstyp;

            if (matobjektId.isPresent()) {
                optMatningstyp = matobjektIdMatningstyper.get(matobjektId.get())
                        .stream()
                        .filter(mt -> mt.getId().equals(im.getMatningstypId()))
                        .findAny();
            } else {
                optMatningstyp = Optional.empty();
            }

            matningService.validateStandard(optMatningstyp, im);
        });
    }

    private void validateInstrument(List<ImportMatning> imports) {
        var instumentIdMatningstyper = imports.stream()
                .map(ImportMatning::getInstrument)
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> matningstypService.findByInstrument(id)));

        var matobjektIdName = instumentIdMatningstyper.values().stream()
                .filter(mts -> mts.size() == 1)
                .map(mts -> mts.get(0).getMatobjektId())
                .distinct()
                .collect(Collectors.toMap(id -> id, id -> matbojektRepository.findNamnById(id).get()));

        imports.forEach(im -> {
            var imm = im.getInstrument();
            var size = instumentIdMatningstyper.get(im.getInstrument()).size();

            if (size < 1) {
                im.setMatobjekt(im.getInstrument());
                im.setMatningstyp(im.getInstrument());
                im.getImportFel().add(ImportError.INSTUMENT_MISSING);
            } else if (size == 1) {
                var matningstyp = instumentIdMatningstyper.get(im.getInstrument()).get(0);
                im.setMatningstyp(matningstyp.getTyp());
                im.setMatningstypId(matningstyp.getId());
                im.setMatobjekt(matobjektIdName.get(matningstyp.getMatobjektId()));
                im.setMatobjektId(matningstyp.getMatobjektId());
            } else {
                im.setMatobjekt(im.getInstrument());
                im.setMatningstyp(im.getInstrument());
                im.getImportFel().add(ImportError.INSTUMENT_TOMANY);
            }
        });

        imports.forEach(im -> {
            var matningstyper = instumentIdMatningstyper.get(im.getInstrument());
            var optMatningstyp = matningstyper.size() == 1 ? Optional.of(matningstyper.get(0)) : Optional.<Matningstyp>empty();
            matningService.validateInstrument(optMatningstyp, im);
        });
    }

    private static LocalDateTime calculateForseningstidpunkt(LocalDateTime starttidpunkt, Short matintervallTidsenhet, Short paminnelseDagar) {
        LocalDateTime forseningstidpunkt = starttidpunkt.plusDays(paminnelseDagar);
        switch (matintervallTidsenhet) {
            case 1: // Dag
                return forseningstidpunkt.plusDays(1);
            case 2: // Vecka
                return forseningstidpunkt.plusWeeks(1);
            case 3: // Månad
                return forseningstidpunkt.plusMonths(1);
            case 4: // År
                return forseningstidpunkt.plusYears(1);
            case 0: // Timme
            default:
                return forseningstidpunkt;
        }
    }

    private MatobjektEntity findMatobjekt(Integer id) throws MatobjektNotFoundException {
        return matbojektRepository.findById(id).orElseThrow(() -> new MatobjektNotFoundException());
    }

    private MatobjektEntity persist(MatobjektEntity entity) {
        return matbojektRepository.saveAndFlush(entity);
    }

    private void purgeRemovedFiles(Matobjekt before, Matobjekt after) {
        try {
            List<UUID> ids = new ArrayList<>();

            if (before.getDokument() != null) {
                if (after.getDokument() != null) {
                    ids.addAll(before.getDokument().stream()
                            .filter(id -> !after.getDokument().contains(id))
                            .collect(Collectors.toList()));
                } else {
                    ids.addAll(before.getDokument());
                }
            }

            if (before.getBifogadBildId() != null && !before.getBifogadBildId().equals(after.getBifogadBildId())) {
                ids.add(before.getBifogadBildId());
            }

            if (!ids.isEmpty()) {
                bifogadfilService.deleteBifogadefiler(ids);
            }
        } catch (BifogadfilNotFoundException e) {
            // suppress
        }
    }

    public Collection<MatobjektMapinfo> getMatobjektMapinfo(MatningstypSearchFilter filter) {
        List<MatobjektMapinfo> list = matobjektJooqRepository.matobjektMapinfo(filter).map(MatobjektMapinfo::new);

        Map<Integer, MatobjektMapinfo> map = new HashMap<>();

        for (MatobjektMapinfo mi : list) {
            if (map.containsKey(mi.getId())) {
                map.get(mi.getId()).getMatningstypIds().addAll(mi.getMatningstypIds());
            } else {
                Integer matningstypId = mi.getMatningstypIds().get(0);
                mi.setMatningstypIds(new ArrayList<>());
                mi.getMatningstypIds().add(matningstypId);
                map.put(mi.getId(), mi);
            }
        }

        return map.values();
    }

    private List<MatningstypMatrunda> getMatrundor(Integer matobjektId) {
        List<Integer> matningstyper = matningstypRepository.findIdsByMatobjektId(matobjektId);
        List<MatrundaMatningstypRecord> matrundor = matobjektJooqRepository.matrundorForMatningstyper(matningstyper);

        List<MatningstypMatrunda> list = new ArrayList<>();
        for (MatrundaMatningstypRecord item : matrundor) {
            list.add(MatningstypMatrunda.fromRecord(item));
        }

        return list;
    }

}
