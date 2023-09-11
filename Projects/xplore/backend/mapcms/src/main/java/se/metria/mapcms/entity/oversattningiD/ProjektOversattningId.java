package se.metria.mapcms.entity.oversattningiD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProjektOversattningId implements Serializable {

    private static final long serialVersionUID = -6493582469667136643L;

    @Column(name = "sprak_kod")
    private String sprakkod;

    @Column(name = "projekt_id")
    private UUID projektId;
}
