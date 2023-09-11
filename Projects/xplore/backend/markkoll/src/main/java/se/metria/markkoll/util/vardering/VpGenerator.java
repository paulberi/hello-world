package se.metria.markkoll.util.vardering;

import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.openapi.model.FiberVarderingConfigDto;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.util.vardering.vpgeneratorstrategy.ElnatVpGeneratorStrategy;
import se.metria.markkoll.util.vardering.vpgeneratorstrategy.FiberVpGeneratorStrategy;
import se.metria.markkoll.util.vardering.vpgeneratorstrategy.VpGeneratorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static se.metria.markkoll.util.AccountUtil.MARKKOLL_SYSTEM_USER_FRIENDLY_NAME;

@Slf4j
@RequiredArgsConstructor
public class VpGenerator<VPDTO, ERSATTNINGDTO> {
    @NonNull
    private final VpGeneratorStrategy<VPDTO, ERSATTNINGDTO> vpGeneratorStrategy;

    public ByteArrayResource
    generateVarderingsprotokoll(VPDTO vpDto,
                                VarderingsprotokollMetadataExtra metadataExtra,
                                List<Signatar> signatarer,
                                String author,
                                String lastModifiedBy) throws IOException, InvalidFormatException {
        var vpFile = new ClassPathResource(vpGeneratorStrategy.getVpPath()).getInputStream();
        var workbook = openWorkbook(vpFile.readAllBytes());

        vpGeneratorStrategy.updateWorkbook(workbook, vpDto, metadataExtra, signatarer);

        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

        workbook.getProperties().getCoreProperties().setCreator(getName(author));
        workbook.getProperties().getCoreProperties().setLastModifiedByUser(getName(lastModifiedBy));
        setModifiedDate(workbook);

        var baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return new ByteArrayResource(baos.toByteArray());
    }

    public ERSATTNINGDTO getErsattning(VPDTO vpDto) throws IOException {
        var vpFile = new ClassPathResource(vpGeneratorStrategy.getVpPath()).getInputStream();
        var workbook = openWorkbook(vpFile.readAllBytes());

        vpGeneratorStrategy.updateWorkbook(workbook, vpDto, null, null);

        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

        try {
            return vpGeneratorStrategy.getErsattningCellValues(workbook);
        } catch (IllegalStateException e) { // Gick inte att läsa från cellen av en eller annan anledning
            log.error(e.getMessage());
            return null;
        }
    }

    public Integer getMaxSignatarer() {
        return vpGeneratorStrategy.getMaxSignatarer();
    }

    public static VpGenerator<ElnatVarderingsprotokollDto, ElnatErsattningDto> elnatVpGenerator() {
        var strategy = new ElnatVpGeneratorStrategy();
        return new VpGenerator<>(strategy);
    }

    public static VpGenerator<FiberVarderingsprotokollDto, FiberErsattningDto>
    fiberVpGenerator(FiberVarderingConfigDto config) {
        var strategy = new FiberVpGeneratorStrategy(config);
        return new VpGenerator<>(strategy);
    }

    private String getName(String name) {
        return !StringUtil.isNullOrEmpty(name) ? name : MARKKOLL_SYSTEM_USER_FRIENDLY_NAME;
    }

    private XSSFWorkbook openWorkbook(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return new XSSFWorkbook(is);
    }

    private void setModifiedDate(XSSFWorkbook workbook) throws InvalidFormatException {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        var now = LocalDateTime.now(ZoneOffset.UTC).format(formatter);
        workbook.getProperties().getCoreProperties().setModified(now);
    }
}
