package se.metria.markkoll.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@Table(name = "niskalla")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Builder
@With
public class NisKallaEntity {
    @Id
    @Column(name = "kund_id")
    String kundId;

    @Column
    boolean dpCom;

    @Column
    boolean dpPower;

    @Column
    boolean trimble;
}
