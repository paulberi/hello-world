package se.metria.skogsmaskindata.service.imports.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
public class Importstatus {
	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Import parent;

	private String status;
	private String cause;
	private OffsetDateTime started;
	private OffsetDateTime completed;

	public Importstatus() {
		status = "IN_PROGRESS";
		started = OffsetDateTime.now();
	}

	public String getStatus() {
		return status;
	}

	public OffsetDateTime getStarted() {
		return started;
	}

	public OffsetDateTime getCompleted() {
		return completed;
	}

	public void setParent(Import parent) {
		this.parent = parent;
	}

	public void setCompleted() {
		status = "COMPLETED";
		completed = OffsetDateTime.now();
	}

	public void setError(String cause) {
		status = "ERROR";
		this.cause = cause;
	}

	public boolean isError() {
		return "ERROR".equals(status);
	}

	public boolean isCompleted() {
		return "COMPLETED".equals(status);
	}

	public String getCause() {
		return cause;
	}
}
