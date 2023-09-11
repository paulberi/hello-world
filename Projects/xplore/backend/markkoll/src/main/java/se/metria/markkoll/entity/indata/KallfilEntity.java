package se.metria.markkoll.entity.indata;

import lombok.Data;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "kallfil")
public class KallfilEntity {
    @Id
    @GeneratedValue
    UUID id;

    @Column(name = "indatatyp")
    IndataTypDto indataTyp;

    @OneToOne
    FilEntity fil;

    @OneToMany(mappedBy = "kallfil")
    List<IndataEntity> indata;
}
