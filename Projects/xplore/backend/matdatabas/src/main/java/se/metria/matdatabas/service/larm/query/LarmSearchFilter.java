package se.metria.matdatabas.service.larm.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LarmSearchFilter {

	private Short larmStatus;
	private List<Integer> larmTillAnvandargruppIds;
	private Integer larmniva;

}
