package se.metria.markkoll.service.vardering.elnat.bilaga;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.markkoll.openapi.model.BilagaTypDto;
import se.metria.markkoll.openapi.model.ElnatBilagaDto;
import se.metria.markkoll.openapi.model.ElnatBilagaTypDto;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElnatBilagaImportService {
    @NonNull
    ElnatBilagaService elnatBilagaService;

    @NonNull
    ElnatVarderingsprotokollService elnatVarderingsprotokollService;

    @NonNull
    HinderAkermarkImport hinderAkermarkImport;

    @Transactional
    public ElnatBilagaDto addBilaga(MultipartFile file, BilagaTypDto bilagaTypDto, UUID vpId) throws IOException {
        var vp = elnatVarderingsprotokollService.get(vpId);

        if (bilagaTypDto == BilagaTypDto.AKERNORM_74) {
            vp = hinderAkermarkImport.onBilagaAdd(file.getResource(), vp);
        }

        var bilaga = elnatVarderingsprotokollService.addBilaga(file, bilagaTypDto, vpId);
        elnatVarderingsprotokollService.update(vp);

        return bilaga;
    }

    public void removeBilaga(UUID bilagaId) {
        var bilaga = elnatBilagaService.get(bilagaId);
        var vp = elnatVarderingsprotokollService.getWithBilagaid(bilagaId);

        if (bilaga.getTyp() == ElnatBilagaTypDto.AKERNORM_74) {
            vp = hinderAkermarkImport.onBilagaRemove(vp);
        }

        elnatVarderingsprotokollService.removeBilaga(bilagaId);
        elnatVarderingsprotokollService.update(vp);
    }
}
