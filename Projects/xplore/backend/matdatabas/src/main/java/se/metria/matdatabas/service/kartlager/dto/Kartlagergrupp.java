package se.metria.matdatabas.service.kartlager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Kartlagergrupp implements Ordered {
	private String namn;
	private List<Kartlager> kartlager;

	@Override
	@JsonIgnore
	public int getOrder() {
		return kartlager.stream().mapToInt(Kartlager::getOrdning).max().orElse(-1);
	}
}
