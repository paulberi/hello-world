package se.metria.xplore.kundconfig.repository.common;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Automatisk hantering av skapat datum, och annan metadata, för entities.
 *
 * Låt dina entities ärva från Auditable, och släng
 * @EntityListeners(AuditingEntityListener.class)
 * på din entityklass.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class Auditable<T> extends BaseEntity<T> {
    @CreatedBy
    @Column(name = "skapad_av")
    private String skapadAv;

    @CreatedDate
    @Column(name = "skapad_datum")
    private LocalDateTime skapadDatum;

    @LastModifiedBy
    @Column(name = "andrad_av")
    private String andradAv;

    @LastModifiedDate
    @Column(name = "andrad_datum")
    private LocalDateTime andradDatum;
}