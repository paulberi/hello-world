package se.metria.xplore.samrad.commons.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Automatisk hantering av skapat datum, och annan metadata, för entities.
 *
 * Låt dina entities ärva från Auditable, och släng
 * {@literal @EntityListeners(AuditingEntityListener.class)}
 * på din entityklass.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Auditable<T> extends BaseEntity<T> {
    @CreatedBy
    @Column(name = "andring_av")
    private String andringAv;

    @CreatedDate
    @Column(name = "andring_datum")
    private LocalDateTime andringDatum;
}
