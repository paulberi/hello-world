package se.metria.matdatabas.service.kartlager.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.matdatabas.service.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static se.metria.matdatabas.service.kartlager.GeoJson.isValidGeoJson;

@Getter
@Setter
@Entity
@Table(name = "kartlager_fil")
@SequenceGenerator(name = "kartlager_fil_id_seq", sequenceName = "kartlager_fil_id_seq", allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
public class KartlagerfilEntity extends BaseEntity<UUID> {
	@Id
	@Column(insertable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	private String filnamn;
	private String fil;
	private String stil;

	@CreatedDate
	private LocalDateTime skapadDatum;

	@ManyToOne
	@JoinColumn(name = "kartlager_id")
	private KartlagerEntity owner;

	public KartlagerfilEntity() {
	}

	public KartlagerfilEntity(String filnamn, String fil, String stil) {
		if (!isValidGeoJson(fil)) {
			throw new IllegalArgumentException("cannot parse geojson file: " + filnamn);
		}
		this.filnamn = filnamn;
		this.fil = fil;
		this.stil = stil;
	}
}
