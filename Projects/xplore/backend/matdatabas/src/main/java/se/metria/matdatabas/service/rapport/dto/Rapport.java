package se.metria.matdatabas.service.rapport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rapport {
    String rubrik;
    String information;
    String lagesbildId;
    List<RapportGraf> grafer;
}
