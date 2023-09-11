package se.metria.xplore.samrad.controllers.adminApi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.metria.xplore.samrad.commons.modelMapper.ModelMapping;
import se.metria.xplore.samrad.commons.utils.HttpUtils;
import se.metria.xplore.samrad.entities.FilEntity;
import se.metria.xplore.samrad.openapi.adminapi.KundFilerApi;
import se.metria.xplore.samrad.openapi.model.FilDto;
import se.metria.xplore.samrad.services.FilService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class KundFilController implements KundFilerApi {


    @NonNull
    private FilService filCrudService;

    @Override
    public ResponseEntity<Resource> getKundFil(String kundId, UUID filId) throws Exception {
        var entity = filCrudService.getFileById(filId);
        var header= HttpUtils.responseHeaders(entity.getFilNamn(), MediaType.APPLICATION_OCTET_STREAM);

        if (entity!=null) {
            ByteArrayResource resource = new ByteArrayResource(entity.getFil());
            return ResponseEntity.ok()
                    .headers(header)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<FilDto>> listKundFiler(String kundId) throws Exception {
        List<FilDto> filDto=new ArrayList<>();
        filDto=filCrudService.allFiles();
        return ResponseEntity.ok().body(filDto);
    }

    @Override
    public ResponseEntity<Void> removeFil(String kundId, UUID filId) throws Exception {
        filCrudService.deleteFile(filId);
        return ResponseEntity.ok().build();
    }



    @Override
    public ResponseEntity<FilDto> saveKundFil(String kundId, MultipartFile file) throws Exception {

        FilEntity filEntity= filCrudService.create(file);
        FilDto filDto=new FilDto();
        ModelMapping.mapAndMerge(filEntity, filDto);
        return  ResponseEntity.ok().body(filDto);
    }

}

