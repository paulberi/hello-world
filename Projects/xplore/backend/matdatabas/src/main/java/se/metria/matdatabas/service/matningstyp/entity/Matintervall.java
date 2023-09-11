package se.metria.matdatabas.service.matningstyp.entity;

import lombok.Getter;
import se.metria.matdatabas.service.matningstyp.MatningstypConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Embeddable
public class Matintervall {
	@NotNull
	@Column(name = "matintervall_tidsenhet")
	private Short tidsenhet;

	@NotNull
	@Column(name = "matintervall_antal_ganger")
	private Short antalGanger;

	public Matintervall() {
	}

	public Matintervall(Short tidsenhet, Short antalGanger) {
		if (antalGanger < 0) {
			throw new IllegalArgumentException("antalGanger must be > 0");
		}
		switch (tidsenhet) {
			case MatningstypConstants.TIDSENHET_TIMME:
				if (antalGanger <= 60) break;
			case MatningstypConstants.TIDSENHET_DAG:
				if (antalGanger <= 24) break;
			case MatningstypConstants.TIDSENHET_VECKA:
				if (antalGanger <= 7) break;
			case MatningstypConstants.TIDSENHET_MANAD:
				if (antalGanger <= 4) break;
			case MatningstypConstants.TIDSENHET_AR:
				if (antalGanger <= 12) break;
			default:
				throw new IllegalArgumentException("Illegal combination of tidsenhet and antalGanger: " + tidsenhet + ", " + antalGanger);
		}
		this.tidsenhet = tidsenhet;
		this.antalGanger = antalGanger;
	}
}
