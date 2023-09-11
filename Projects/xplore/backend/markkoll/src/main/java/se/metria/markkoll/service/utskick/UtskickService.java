package se.metria.markkoll.service.utskick;

import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.projekt.UtskickProjektView;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.utskick.utskickvpfn.UtskickVpFn;
import se.metria.markkoll.service.utskick.utskickvpfn.UtskickVpFnElnat;
import se.metria.markkoll.service.utskick.utskickvpfn.UtskickVpFnEmpty;
import se.metria.markkoll.service.utskick.utskickvpfn.UtskickVpFnFiber;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import java.util.*;
import java.util.stream.Collectors;

@MarkkollService
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UtskickService {
    @NonNull
    private final AvtalService avtalService;

    @NonNull
    private final FastighetService fastighetService;

    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final ProjektRepository projektRepository;

    public UtskickDto getInfobrevUtskick(UUID avtalId) {
        var utskicksnummer = avtalService.getUtskicksnummer(avtalId);
        var signatarer = markagareService.getAgareForAvtal(avtalId);

        var kontaktperson = filterKontaktperson(signatarer);

        return UtskickDto.builder()
            .lopnummer(String.valueOf(utskicksnummer))
            .signatarer(signatarer)
            .kontaktperson(kontaktperson)
            .build();
    }

    public UtskickBatchDto getAvtalUtskickBatch(UUID avtalId) {
        var agare = filterAgareInkluderade(avtalId);
        var lagfarnaAgare = filterAgareLagfarna(agare);
        var ombud = filterAgareOmbud(agare);
        var kontaktpersonAvtal = filterKontaktperson(agare);

        var utskicksnummer = avtalService.updateUtskicksnummer(avtalId);
        var fastighet = fastighetService.getFastighet(avtalId);
        var projektinfo = projektRepository.getUtskickProjektInfo(avtalId);

        var title = DokumentUtil.createFilename(FileType.NONE, fastighet.getFastighetsbeteckning());

        var utskickVpFn = getUtskickVpFn(projektinfo);
        var utskickList = getUtskickList(lagfarnaAgare, ombud, kontaktpersonAvtal, fastighet,
            projektinfo.getUtskicksstrategi(), utskicksnummer, utskickVpFn);

        return UtskickBatchDto.builder()
            .title(title)
            .utskick(utskickList)
            .build();
    }

    private String adressString(MarkagareDto ag) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(ag.getAdress().trim());
        joiner.add(ag.getPostort().trim());
        return joiner.toString().trim().toLowerCase();
    }

    private String calculateLopnummer(Integer utskicksnummer, Integer signatarnummer) {
        var signatarnummerString = calculateSignatarnummer(signatarnummer);
        return utskicksnummer + signatarnummerString;
    }

    private String calculateSignatarnummer(Integer signatarnummer) {
        final var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";
        final var size = alphabet.length();

        if (signatarnummer < size) {
            return String.valueOf(alphabet.charAt(signatarnummer));
        }
        else {
            return calculateSignatarnummer((signatarnummer / size) - 1) + alphabet.charAt(signatarnummer % size);
        }
    }

    private List<MarkagareDto> filterAgareInkluderade(UUID avtalId) {
        var agare = markagareService.getAgareForAvtal(avtalId).stream()
            .filter(MarkagareDto::getInkluderaIAvtal)
            .collect(Collectors.toList());

        if (agare.size() == 0) {
            log.warn("Inga fastighetsägare inkluderade i utskick");
        }

        return agare;
    }

    private List<MarkagareDto> filterAgareLagfarna(List<MarkagareDto> agare) {
        return agare.stream()
            .filter(ag -> ag.getAgartyp() == AgartypDto.LF)
            .filter(ag -> !StringUtil.isNullOrEmpty(ag.getAndel()))
            .collect(Collectors.toList());
    }

    private List<MarkagareDto> filterAgareOmbud(List<MarkagareDto> agare) {
        return agare.stream()
            .filter(ag -> ag.getAgartyp() == AgartypDto.OMBUD)
            .collect(Collectors.toList());
    }

    private Optional<MarkagareDto> filterKontaktperson(List<MarkagareDto> agare) {
        return agare.stream()
            .filter(ag -> ag.getKontaktperson() != null && ag.getKontaktperson())
            .findAny().or(() -> Optional.ofNullable(agare.size() > 0 ? agare.get(0) : null));
    }

    private UtskickDto
    getUtskickDtoOmbudStrategiFastighet(MarkagareDto ombud, String lopnummer, Optional<MarkagareDto> kontaktpersonAvtal)
    {
        return UtskickDto.builder()
            .signatarer(Arrays.asList(ombud))
            .titel(DokumentUtil.createFilename(FileType.NONE, ombud.getNamn()))
            .kontaktperson(kontaktpersonAvtal)
            .lopnummer(lopnummer)
            .build();
    }

    private UtskickDto
    getUtskickDtoOmbudStrategiInteFastighet(MarkagareDto ombud,
                                            String lopnummer,
                                            Optional<MarkagareDto> kontaktpersonAvtal)
    {
        return UtskickDto.builder()
            .signatarer(Arrays.asList(ombud))
            .titel(DokumentUtil.createFilename(FileType.NONE, ombud.getNamn()))
            .kontaktperson(Optional.of(ombud))
            .lopnummer(lopnummer)
            .build();
    }

    private UtskickVpFn getUtskickVpFn(UtskickProjektView projektInfo) {
        if (!projektInfo.getShouldHaveVp()) {
            return new UtskickVpFnEmpty();
        }
        else switch (projektInfo.getProjektTyp()) {
            case FIBER:
                return new UtskickVpFnFiber();
            case LOKALNAT:
            case REGIONNAT:
                return new UtskickVpFnElnat();
            default:
                throw new IllegalArgumentException("Okänd projekttyp: " + projektInfo.getProjektTyp());
        }
    }

    private UtskickDto
    getUtskickDtoStrategiAdress(FastighetDto fastighet,
                                List<MarkagareDto> agareAtAdress,
                                String lopnummer,
                                Optional<MarkagareDto> kontaktpersonAvtal)
    {
        var kontaktpersonUtskick = kontaktpersonAvtal
            .flatMap(ma -> agareAtAdress.stream().filter(ag -> ag == ma).findAny());

        return UtskickDto.builder()
            .signatarer(agareAtAdress)
            .titel(DokumentUtil.createFilename(FileType.NONE, adressString(agareAtAdress.get(0))))
            .kontaktperson(kontaktpersonUtskick.isPresent() ? kontaktpersonUtskick : Optional.of(agareAtAdress.get(0)))
            .lopnummer(lopnummer)
            .build();
    }

    private UtskickDto
    getUtskickDtoStrategiFastighetagare(FastighetDto fastighet,
                                        List<MarkagareDto> lf,
                                        String lopnummer,
                                        Optional<MarkagareDto> kontaktpersonAvtal)
    {
        return UtskickDto.builder()
            .signatarer(lf)
            .titel(DokumentUtil.createFilename(FileType.NONE, lf.get(0).getNamn()))
            .kontaktperson(Optional.of(lf.get(0)))
            .lopnummer(lopnummer)
            .build();
    }

    private UtskickDto
    getUtskickDtoStrategiFastighet(FastighetDto fastighet,
                                   List<MarkagareDto> lagfarnaAgare,
                                   String lopnummer,
                                   Optional<MarkagareDto> kontaktpersonAvtal)
    {
        return UtskickDto.builder()
            .signatarer(lagfarnaAgare)
            .titel(DokumentUtil.createFilename(FileType.NONE, fastighet.getFastighetsbeteckning()))
            .kontaktperson(kontaktpersonAvtal)
            .lopnummer(lopnummer)
            .build();
    }

    private List<UtskickDto>
    getUtskickList(List<MarkagareDto> lagfarnaAgare,
                   List<MarkagareDto> ombud,
                   Optional<MarkagareDto> kontaktpersonAvtal,
                   FastighetDto fastighet,
                   UtskicksstrategiDto utskicksstrategi,
                   Integer utskicksnummer,
                   UtskickVpFn utskickVpFn)
    {
        switch(DetaljtypDto.fromValue(fastighet.getDetaljtyp())) {
            case SAMF:
            case SAMFO:
                return getUtskickListStrategiFastighet(fastighet, lagfarnaAgare, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
            case FASTIGHET:
            case FASTO:
                return getUtskickListDetaljtypFastighet(fastighet, lagfarnaAgare, ombud, kontaktpersonAvtal, utskicksstrategi,
                    utskicksnummer, utskickVpFn);
            default:
                throw new IllegalArgumentException("Okänd utskicksstrategi: " + utskicksstrategi);
        }
    }

    private List<UtskickDto>
    getUtskickListAbstract(UtskickDtoFn utskickDtoFn,
                           FastighetDto fastighetDto,
                           List<List<MarkagareDto>> lagfarnaAgare,
                           UtskickDtoOmbudFn utskickDtoOmbudFn,
                           List<MarkagareDto> ombud,
                           Optional<MarkagareDto> kontaktperson,
                           Integer utskicksnummer,
                           UtskickVpFn utskickVpFn
    )
    {
        var utskick = new ArrayList<UtskickDto>();

        if (lagfarnaAgare.size() + ombud.size() == 1) {
            if (lagfarnaAgare.size() == 1) {
                utskick.add(utskickDtoFn.apply(fastighetDto, lagfarnaAgare.get(0), utskicksnummer.toString(), kontaktperson));
            }
            else {
                utskick.add(utskickDtoOmbudFn.apply(ombud.get(0), utskicksnummer.toString(), kontaktperson));
            }
        }
        else {
            var signatarnummer = new MutableInt(0);

            for (var i = 0; i < lagfarnaAgare.size(); i++) {
                var lopnummer = calculateLopnummer(utskicksnummer, signatarnummer.getAndIncrement());
                utskick.add(utskickDtoFn.apply(fastighetDto, lagfarnaAgare.get(i), lopnummer, kontaktperson));
            }

            for (var i = 0; i < ombud.size(); i++) {
                var lopnummer = calculateLopnummer(utskicksnummer, signatarnummer.getAndIncrement());
                utskick.add(utskickDtoOmbudFn.apply(ombud.get(i), lopnummer, kontaktperson));
            }
        }

        utskick.forEach(u -> u.setUtskickVp(utskickVpFn.get(u.getSignatarer(), u.getTitel(), u.getKontaktperson())));

        uniqueifyVpSignatarer(utskick);
        return uniqueifyTitles(utskick);
    }

    private void uniqueifyVpSignatarer(List<UtskickDto> utskick) {
        var signatarerMap = utskick.stream()
            .map(u -> u.getUtskickVp())
            .flatMap(Collection::stream)
            .collect(Collectors.groupingBy(sig -> sig.getTitle()));

        for (var entry: signatarerMap.entrySet()) {
            var counter = 1;
            if (entry.getValue().size() != 1) {
                for (var sig: entry.getValue()) {
                    sig.setTitle(sig.getTitle() + counter++);
                }
            }
        }
    }

    /* Viktigt att avtalen och andra filer som kommer att bli resultatet av utskicken får unika namn, när vi knör in dem
    i zipfiler */
    private List<UtskickDto> uniqueifyTitles(List<UtskickDto> utskick) {
        var utskickMap = utskick.stream()
            .collect(Collectors.groupingBy(u -> u.getTitel()));

        return utskickMap.entrySet().stream().map(entry -> {
            if (entry.getValue().size() != 1) {
                var counter = 1;
                for (var u : entry.getValue()) {
                    u.setTitel(u.getTitel() + counter++);
                }
            }
            return entry.getValue();
        })
            .flatMap(Collection::stream)
            .sorted(Comparator.comparing(UtskickDto::getLopnummer))
            .collect(Collectors.toList());
    }

    private List<UtskickDto>
    getUtskickListDetaljtypFastighet(FastighetDto fastighet,
                                     List<MarkagareDto> lagfarnaAgare,
                                     List<MarkagareDto> ombud,
                                     Optional<MarkagareDto> kontaktpersonAvtal,
                                     UtskicksstrategiDto utskicksstrategi,
                                     Integer utskicksnummer,
                                     UtskickVpFn utskickVpFn)
    {
        switch(utskicksstrategi) {
            case FASTIGHET:
                return getUtskickListStrategiFastighet(fastighet, lagfarnaAgare, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
            case FASTIGHETSAGARE:
                return getUtskickListStrategiFastighetsagare(fastighet, lagfarnaAgare, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
            case ADRESS:
                return getUtskickListStrategiAdress(fastighet, lagfarnaAgare, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
            default:
                throw new IllegalArgumentException("Okänd utskicksstrategi: " + utskicksstrategi);
        }
    }

    private List<UtskickDto>
    getUtskickListStrategiAdress(FastighetDto fastighet,
                                 List<MarkagareDto> lagfarnaAgare,
                                 List<MarkagareDto> ombud,
                                 Optional<MarkagareDto> kontaktpersonAvtal,
                                 Integer utskicksnummer,
                                 UtskickVpFn utskickVpFn)
    {
        var lagfarnaAgareAtAdressess = new ArrayList<>(lagfarnaAgare.stream()
            .collect(Collectors.groupingBy(this::adressString))
            .values()
        );

        return getUtskickListAbstract(this::getUtskickDtoStrategiAdress, fastighet, lagfarnaAgareAtAdressess,
            this::getUtskickDtoOmbudStrategiInteFastighet, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
    }

    private List<UtskickDto>
    getUtskickListStrategiFastighetsagare(FastighetDto fastighet,
                                          List<MarkagareDto> lagfarnaAgare,
                                          List<MarkagareDto> ombud,
                                          Optional<MarkagareDto> kontaktpersonAvtal,
                                          Integer utskicksnummer,
                                          UtskickVpFn utskickVpFn)
    {
        var lagfarnaAgareList = lagfarnaAgare
            .stream().map(Arrays::asList)
            .collect(Collectors.toList());

        return getUtskickListAbstract(this::getUtskickDtoStrategiFastighetagare, fastighet, lagfarnaAgareList,
            this::getUtskickDtoOmbudStrategiInteFastighet, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
    }

    private List<UtskickDto>
    getUtskickListStrategiFastighet(FastighetDto fastighet,
                                    List<MarkagareDto> lagfarnaAgare,
                                    List<MarkagareDto> ombud,
                                    Optional<MarkagareDto> kontaktpersonAvtal,
                                    Integer utskicksnummer,
                                    UtskickVpFn utskickVpFn)
    {
        if (lagfarnaAgare.isEmpty()) {
            return getUtskickListAbstract(this::getUtskickDtoStrategiFastighet, fastighet, new ArrayList<>(),
                this::getUtskickDtoOmbudStrategiFastighet, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
        }
        else {
            return getUtskickListAbstract(this::getUtskickDtoStrategiFastighet, fastighet, Arrays.asList(lagfarnaAgare),
                this::getUtskickDtoOmbudStrategiFastighet, ombud, kontaktpersonAvtal, utskicksnummer, utskickVpFn);
        }
    }
}
