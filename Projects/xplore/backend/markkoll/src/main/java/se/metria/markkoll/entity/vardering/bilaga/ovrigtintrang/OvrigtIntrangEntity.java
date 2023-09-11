package se.metria.markkoll.entity.vardering.bilaga.ovrigtintrang;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "ovrigt_intrang")
public class OvrigtIntrangEntity {
    @Id
    @GeneratedValue
    UUID id;

    String beskrivning;
}
