package se.metria.matdatabas.service.matrunda.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MatrundaMatningstypEntity  {

	@NotNull
	@Column(name = "matningstyp_id")
	private Integer matningstypId;

	@NotNull
	private Short ordning;

}
