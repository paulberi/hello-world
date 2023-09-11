package se.metria.markkoll.util.vardering.vpgeneratorstrategy;

import org.apache.poi.ss.usermodel.Workbook;
import se.metria.markkoll.util.vardering.Signatar;
import se.metria.markkoll.util.vardering.VarderingsprotokollMetadataExtra;

import java.util.List;

public interface VpGeneratorStrategy<VPDTO, ERSATTNINGDTO> {
    ERSATTNINGDTO getErsattningCellValues(Workbook workbook);

    Integer getMaxSignatarer();

    String getVpPath();

    void updateWorkbook(Workbook workbook,
                        VPDTO vpDto,
                        VarderingsprotokollMetadataExtra metadataExtra,
                        List<Signatar> signatarer);
}
