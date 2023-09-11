package se.metria.matdatabas.service.larm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.larm.entity.LarmnivaEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveLarmniva {
	private String namn;
	private String beskrivning;
}
