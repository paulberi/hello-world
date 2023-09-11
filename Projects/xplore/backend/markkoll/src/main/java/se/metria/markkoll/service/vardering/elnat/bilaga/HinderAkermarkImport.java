package se.metria.markkoll.service.vardering.elnat.bilaga;

import com.google.common.math.DoubleMath;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.ElnatHinderAkermarkDto;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.util.dokument.ExcelUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class HinderAkermarkImport {
    public ElnatVarderingsprotokollDto onBilagaAdd(Resource bilaga, ElnatVarderingsprotokollDto vp) throws IOException {
        try {
            var workbook = ExcelUtil.openWorkbook(bilaga.getInputStream().readAllBytes());
            var ersattning = ExcelUtil.getDoubleCellValue(workbook, 'I', 45);

            verify(workbook, ersattning);

            return vp.hinderAkermark(Arrays.asList(
                new ElnatHinderAkermarkDto().ersattning(ersattning.intValue())
            ));
        } catch(POIXMLException e) {
            throw new MarkkollException(MarkkollError.BILAGA_ERROR_OPEN_FILE, e);
        } catch(IllegalStateException e) {
            throw new MarkkollException(MarkkollError.BILAGA_ERROR_VALUE, e);
        }
    }

    public ElnatVarderingsprotokollDto onBilagaRemove(ElnatVarderingsprotokollDto vp) {
        return vp.hinderAkermark(new ArrayList<>());
    }

    private void verify(Workbook workbook, double ersattning) {
        double sum = 0;
        for (int i = 0; i < 10; i++) {
            try {
                var val = ExcelUtil.getDoubleCellValue(workbook, 'I', 10 + i);
                sum += val;
            } catch(IllegalStateException e) {
                sum += 0; //tom cell
            }
        }

        if (!DoubleMath.fuzzyEquals(sum, ersattning, 0.001)) {
            throw new MarkkollException(MarkkollError.BILAGA_ERROR_VALIDATION);
        }
    }
}
