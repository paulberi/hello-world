package se.metria.matdatabas.restapi.kallsystem;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.KallsystemApi;
import se.metria.matdatabas.openapi.model.KallsystemDto;
import se.metria.matdatabas.service.kallsystem.KallsystemService;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "/api")
@RestController
public class KallsystemController implements KallsystemApi {
    KallsystemService kallsystemService;
    ModelMapper mapper;

    public KallsystemController(KallsystemService kallsystemService) {
        this.kallsystemService = kallsystemService;

        this.mapper = new ModelMapper();
    }

    @Override
    public ResponseEntity<List<KallsystemDto>> kallsystemGet() {
        List<Kallsystem> kallsystem = kallsystemService.findAll();
        return ResponseEntity.ok(kallsystem.stream()
        .map(ks -> mapper.map(ks, KallsystemDto.class))
        .collect(Collectors.toList()));
    }
}
