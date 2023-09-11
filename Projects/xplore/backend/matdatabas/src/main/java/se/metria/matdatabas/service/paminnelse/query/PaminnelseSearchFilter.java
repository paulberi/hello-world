package se.metria.matdatabas.service.paminnelse.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaminnelseSearchFilter {
	private Boolean onlyForsenade;

}
