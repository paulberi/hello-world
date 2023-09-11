package se.metria.markkoll.util.vardering.vpgeneratorstrategy;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.util.dokument.ExcelUtil;
import se.metria.markkoll.util.vardering.FiberErsattningDto;
import se.metria.markkoll.util.vardering.Signatar;
import se.metria.markkoll.util.vardering.VarderingsprotokollMetadataExtra;

import java.util.List;

import static se.metria.markkoll.util.dokument.ExcelUtil.setCellValue;

@Slf4j
@RequiredArgsConstructor
public class FiberVpGeneratorStrategy implements VpGeneratorStrategy<FiberVarderingsprotokollDto, FiberErsattningDto> {
    public static final Integer FASTIGHETSAGARE_MAX = 1;
    private static final Integer MARKLEDNING_MAX = 4;
    private static final Integer PUNKTERSATTNING_MAX = 4;
    private static final Integer INTRANG_AKER_OCH_SKOGSMARK_MAX = 2;
    private static final Integer OVRIG_INTRANGSERSATTNING_MAX = 4;

    @NonNull
    private final FiberVarderingConfigDto vpConfig;

    @Override
    public FiberErsattningDto getErsattningCellValues(Workbook workbook) {
        var ersattning = new FiberErsattningDto();

        try {
            ersattning.setTotal(ExcelUtil.getIntegerCellValue(workbook, 'J', 42));
        } catch (IllegalStateException e) {
            log.warn("Misslyckas med att total ersättning från cell på position (J42), sätter värdet till 0");
            ersattning.setTotal(0);
        }

        return ersattning;
    }

    @Override
    public Integer getMaxSignatarer() {
        return 1;
    }

    @Override
    public String getVpPath() {
        return "värderingsprotokoll/varderingsprotokoll-fiber-v6.xlsx";
    }

    @Override
    public void
    updateWorkbook(Workbook workbook,
                   FiberVarderingsprotokollDto fiberVarderingsprotokollDto,
                   VarderingsprotokollMetadataExtra metadataExtra,
                   List<Signatar> signatarer)
    {
        setVpConfig(workbook);
        setMarkledningValue(workbook, fiberVarderingsprotokollDto.getMarkledning());
        setPunktersattningValue(workbook, fiberVarderingsprotokollDto.getPunktersattning());
        setIntrangAkerOchSkogsmarkValue(workbook, fiberVarderingsprotokollDto.getIntrangAkerOchSkogsmark());
        setOvrigIntrangsersattning(workbook, fiberVarderingsprotokollDto.getOvrigIntrangsersattning());
        setMetadata(workbook, fiberVarderingsprotokollDto.getMetadata());
        setMetadataExtra(workbook, metadataExtra);
        setSignatarer(workbook, signatarer);
    }

    private void setMarkledningValue(Workbook workbook, List<FiberMarkledningDto> markledning) {
        if (markledning == null) {
            return;
        }
        if (markledning.size() > MARKLEDNING_MAX) {
            throw rowErrorException("ERSÄTTNING FÖR OPTORÖR I ÅKER, BETE, JORDBRUKSIMPEDIMENT",
                markledning.size(), MARKLEDNING_MAX);
        }

        for (int i = 0; i < markledning.size(); i++) {
            setCellValue(workbook, 'B', 12 + i, markledningTypValue(markledning.get(i)));
            setCellValue(workbook, 'I', 12 + i, markledning.get(i).getLangd());
        }
    }

    private void setPunktersattningValue(Workbook workbook, List<FiberPunktersattningDto> punktersattning) {
        if (punktersattning == null) {
            return;
        }
        if (punktersattning.size() > PUNKTERSATTNING_MAX) {
            throw rowErrorException("2. SCHABLONERSÄTTNING FÖR MARKSKÅP, OPTOBRUNNAR OCH SITE/TEKNIKBODAR",
                punktersattning.size(), PUNKTERSATTNING_MAX);
        }

        for (int i = 0; i < punktersattning.size(); i++) {
            setCellValue(workbook, 'B', 18 + i, punktersattning.get(i).getBeskrivning());
            setCellValue(workbook, 'G', 18 + i, punktersattningTypValue(punktersattning.get(i).getTyp()));
            setCellValue(workbook, 'I', 18 + i, punktersattning.get(i).getAntal());
        }
    }

    private void setIntrangAkerOchSkogsmarkValue(Workbook workbook, List<FiberIntrangAkerOchSkogsmarkDto> intrangAkerOchSkogsmarkDtos) {
        if (intrangAkerOchSkogsmarkDtos == null) {
            return;
        }
        if (intrangAkerOchSkogsmarkDtos.size() > INTRANG_AKER_OCH_SKOGSMARK_MAX) {
            throw rowErrorException("ERSÄTTNING FÖR INTRÅNG I ÅKER- OCH SKOGSMARK",intrangAkerOchSkogsmarkDtos.size(),
                INTRANG_AKER_OCH_SKOGSMARK_MAX);
        }

        for (int i = 0; i < intrangAkerOchSkogsmarkDtos.size(); i++) {
            setCellValue(workbook, 'B', 25 + i, intrangAkerOchSkogsmarkDtos.get(i).getBeskrivning());
            setCellValue(workbook, 'J', 25 + i, intrangAkerOchSkogsmarkDtos.get(i).getErsattning());
        }
    }

    private void setOvrigIntrangsersattning(Workbook workbook, List<FiberOvrigIntrangsersattningDto> ovrigIntrangsersattningDtos) {
        if (ovrigIntrangsersattningDtos == null) {
            return;
        }
        if (ovrigIntrangsersattningDtos.size() > OVRIG_INTRANGSERSATTNING_MAX) {
            throw rowErrorException("ERSÄTTNING FÖR ÖVRIG INTRÅNGSERSÄTTNING", ovrigIntrangsersattningDtos.size(),
                OVRIG_INTRANGSERSATTNING_MAX);
        }

        for (int i = 0; i < ovrigIntrangsersattningDtos.size(); i++) {
            setCellValue(workbook, 'B', 30 + i, ovrigIntrangsersattningDtos.get(i).getBeskrivning());
            setCellValue(workbook, 'J', 30 + i, ovrigIntrangsersattningDtos.get(i).getErsattning());
        }
    }

    private void
    setMetadata(Workbook workbook,
                FiberVarderingsprotokollMetadataDto metadata)
    {
        if (metadata == null) {
            return;
        }

        setCellValue(workbook, 'I', 6, metadata.getVarderingstidpunkt());
        setCellValue(workbook, 'I', 7, metadata.getVarderingsmanOchForetag());
    }

    private void
    setMetadataExtra(Workbook workbook, VarderingsprotokollMetadataExtra metadataExtra) {
        if (metadataExtra == null) {
            return;
        }

        setCellValue(workbook, 'E', 5, metadataExtra.getFastighetsbeteckning());
        setCellValue(workbook, 'E', 6, metadataExtra.getKommun());
        setCellValue(workbook, 'E', 8, metadataExtra.getKontaktpersonOchAdress());
        setCellValue(workbook, 'I', 5, metadataExtra.getProjektnummer());
    }

    private void setSignatarer(Workbook workbook, List<Signatar> signatarer) {
        if (signatarer == null) {
            return;
        }

        if (signatarer.size() > FASTIGHETSAGARE_MAX) {
            throw rowErrorException("Fastighetsägare", signatarer.size(), FASTIGHETSAGARE_MAX);
        }

        for (int i = 0; i < signatarer.size(); i++) {
            var namn = signatarer.get(i).getNamn();
            var personnummer = signatarer.get(i).getPersonnummer();

            var namnString = namn == null ? "" : namn;
            var personnummerString = personnummer == null || personnummer.equals("") ? "" : String.format(" (%s)", personnummer);
            var personuppgifter = namnString + personnummerString;
            var adress = signatarer.get(i).getAdress();

            setCellValue(workbook, 'B', 46, personuppgifter);
            setCellValue(workbook, 'F', 46, signatarer.get(i).getAndel());
            // Sätt kontaktperson och adress
            if(i == 0){
                setCellValue(workbook, 'E', 8, namnString + " " +  adress);
            }
        }
    }

    private String
    markledningTypValue(FiberMarkledningDto fiberMarkledningDto){
        if(fiberMarkledningDto == null){
            return null;
        }
        switch(fiberMarkledningDto.getBredd()){
            case 1:
                return "Schablonersättning Optorör - bredd 1m";
            case 2:
                return "Schablonersättning Optorör - bredd 2m";
            default:
                throw new IllegalArgumentException("Fel på markledning: " + fiberMarkledningDto.getBeskrivning());
        }
    }

    private void setVpConfig(Workbook workbook) {
        setCellValue(workbook, 1, 'F', 4, vpConfig.getMarkskap().getSkog());
        setCellValue(workbook, 1, 'F', 5, vpConfig.getMarkskap().getJordbruksimpediment());
        setCellValue(workbook, 1, 'F', 6, vpConfig.getMarkskap().getOvrigMark());

        setCellValue(workbook, 1, 'F', 7, vpConfig.getOptobrunn().getSkog());
        setCellValue(workbook, 1, 'F', 8, vpConfig.getOptobrunn().getJordbruksimpediment());
        setCellValue(workbook, 1, 'F', 9, vpConfig.getOptobrunn().getOvrigMark());

        setCellValue(workbook, 1, 'F', 10, vpConfig.getTeknikbod().getSkog6x6m());
        setCellValue(workbook, 1, 'F', 11, vpConfig.getTeknikbod().getSkog8x8m());
        setCellValue(workbook, 1, 'F', 12, vpConfig.getTeknikbod().getSkog8x8m());

        setCellValue(workbook, 1, 'F', 13, vpConfig.getTeknikbod().getJordbruksimpediment6x6m());
        setCellValue(workbook, 1, 'F', 14, vpConfig.getTeknikbod().getJordbruksimpediment8x8m());
        setCellValue(workbook, 1, 'F', 15, vpConfig.getTeknikbod().getJordbruksimpediment10x10m());

        setCellValue(workbook, 1, 'F', 16, vpConfig.getTeknikbod().getOvrigMark6x6m());
        setCellValue(workbook, 1, 'F', 17, vpConfig.getTeknikbod().getOvrigMark8x8m());
        setCellValue(workbook, 1, 'F', 18, vpConfig.getTeknikbod().getOvrigMark10x10m());

        setCellValue(workbook, 1, 'F', 20, vpConfig.getSchablonersattning().getOptoror1m());
        setCellValue(workbook, 1, 'F', 21, vpConfig.getSchablonersattning().getOptoror2m());

        setCellValue(workbook, 1, 'F', 23, vpConfig.getGrundersattning());
        setCellValue(workbook, 1, 'F', 24, vpConfig.getMinimiersattning());

        setCellValue(workbook, 1, 'F', 25, vpConfig.getMinimiersattningEnbartMarkledning() ? "Ja" : "Nej");

        setCellValue(workbook, 1, 'F', 27, vpConfig.getTillaggExpropriationslagen() / 100.);
        setCellValue(workbook, 1, 'F', 28, vpConfig.getSarskildErsattning() / 100.);

        setCellValue(workbook, 1, 'F', 29, vpConfig.getSarskildErsattningMaxbelopp());
    }

    private String
    punktersattningTypValue(FiberPunktersattningTypDto fiberPunktersattningTypDto)
    {
        if (fiberPunktersattningTypDto == null) {
            return null;
        }

        switch (fiberPunktersattningTypDto) {
            case MARKSKAP_JORDBRUKSIMPEDIMENT:
                return "Markskåp - Jordbruksimp.";
            case MARKSKAP_OVRIGMARK:
                return "Markskåp - Övrig mark";
            case MARKSKAP_SKOG:
                return "Markskåp - Skog";

            case OPTOBRUNN_JORDBRUKSIMPEDIMENT:
                return "Optobrunn - Jordbruksimp.";
            case OPTOBRUNN_OVRIGMARK:
                return "Optobrunn - Övrig mark";
            case OPTOBRUNN_SKOG:
                return "Optobrunn - Skog";

            case SITE_JORDBRUKSIMPEDIMENT_6X6M:
                return "Teknikbod - Jordbruksimp. (yta 6x6 meter)";
            case SITE_JORDBRUKSIMPEDIMENT_8X8M:
                return "Teknikbod - Jordbruksimp. (yta 8x8 meter)";
            case SITE_JORDBRUKSIMPEDIMENT_10X10M:
                return "Teknikbod - Jordbruksimp. (yta 10x10 meter)";
            case SITE_OVRIGMARK_6X6M:
                return "Teknikbod - Övrig mark (yta 6x6 meter)";
            case SITE_OVRIGMARK_8X8M:
                return "Teknikbod - Övrig mark (yta 8x8 meter)";
            case SITE_OVRIGMARK_10X10M:
                return "Teknikbod - Övrig mark (yta 10x10 meter)";
            case SITE_SKOG_6X6M:
                return "Teknikbod - Skog (yta 6x6 meter)";
            case SITE_SKOG_8X8M:
                return "Teknikbod - Skog (yta 8x8 meter)";
            case SITE_SKOG_10X10M:
                return "Teknikbod - Skog (yta 10x10 meter)";

            case MARKSKAP_EJ_KLASSIFICERAD:
            case OPTOBRUNN_EJ_KLASSIFICERAD:
            case SITE_EJ_KLASSIFICERAD:
                return null;

            default:
                throw new MarkkollException("Okänd punktersättningstyp: " + fiberPunktersattningTypDto, new IllegalArgumentException());
        }
    }

    private MarkkollException rowErrorException(String type, Integer actual, Integer max) {
        log.error("För många rader ({}) i ({}). Högst {} tillåtna.", actual, type, max);

        return new MarkkollException(MarkkollError.AVTAL_CREATE_ERROR, new IllegalArgumentException());
    }
}
