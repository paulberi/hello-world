package se.metria.matdatabas.service.kallsystem;

import org.springframework.stereotype.Service;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KallsystemService {
    KallsystemRepository repository;

    public KallsystemService(KallsystemRepository repository) {
        this.repository = repository;
    }

    @RolesAllowed(MatdatabasRole.MATRAPPORTOR)
    public Optional<Kallsystem> findById(String namn) {
        return repository.findById(namn).map(Kallsystem::fromEntity);
    }

    @RolesAllowed(MatdatabasRole.MATRAPPORTOR)
    public List<Kallsystem> findAll() {
        return repository.findAll().stream()
        .map(Kallsystem::fromEntity)
                .collect(Collectors.toList());
    }
}
