package se.metria.matdatabas.service.kallsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.matdatabas.service.kallsystem.entity.KallsystemEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kallsystem {
    String namn;

    String beskrivning;
    boolean defaultGodkand;
    boolean manuellImport;
    String tips;

    public static Kallsystem fromEntity(KallsystemEntity entity) {
        return new Kallsystem(
                entity.getNamn(),
                entity.getBeskrivning(),
                entity.isDefaultGodkand(),
                entity.isManuellImport(),
                entity.getTips());
    }
}
