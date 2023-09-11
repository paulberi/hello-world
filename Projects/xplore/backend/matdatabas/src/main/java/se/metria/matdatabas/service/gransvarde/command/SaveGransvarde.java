package se.metria.matdatabas.service.gransvarde.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.gransvarde.entity.GransvardeEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveGransvarde {
	private Integer matningstypId;
	private Short typAvKontroll;
	private Double gransvarde;
	private Integer larmnivaId;
	private Integer larmTillAnvandargruppId;
	private Boolean aktiv;
}
