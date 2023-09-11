package se.metria.markkoll.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.repository.common.Auditable;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "dokumentmall")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
@With
public class DokumentmallEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue
    UUID id;

    String kundId;

    DokumentTypDto dokumenttyp;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    FilEntity fil;
    
    String namn;
    
    Boolean selected;
}
