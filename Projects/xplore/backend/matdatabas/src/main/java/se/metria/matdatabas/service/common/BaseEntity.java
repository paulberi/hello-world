package se.metria.matdatabas.service.common;

import javax.persistence.MappedSuperclass;

/**
 * Implements a base equals and hashCode that is usable for entities
 * that do not have a natural key to base it on. If we base equals and hashCode solely on the primary
 * key, we will have inconsistent behaviour across different JPA states
 * (e.g. NEW -> PERSISTED) because the id changes.
 *
 * Also, keeping entities in sets will be problematic if more than one has a null key.
 *
 * https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
 */
@MappedSuperclass
public abstract class BaseEntity<T> {
	public abstract T getId();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof BaseEntity))
			return false;

		@SuppressWarnings("unchecked")
		BaseEntity<T> other = (BaseEntity<T>) o;

		return getId() != null &&
				getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return 31;
	}
}
