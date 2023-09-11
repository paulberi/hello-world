package se.metria.skogsmaskindata.service.imports.model.feature;

import se.metria.skogsmaskindata.service.imports.model.Import;

import javax.persistence.*;

@MappedSuperclass
public abstract class ForestlinkFeature {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "import_id")
	private Import parent;

	public Import getParent() {
		return parent;
	}

	public void setParent(Import parent) {
		this.parent = parent;
	}
}
