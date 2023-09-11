package se.metria.markkoll.entity.samfallighet;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SamfallighetFastighetId implements Serializable {
    UUID fastighet_id;
    UUID samfallighet_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SamfallighetFastighetId omr = (SamfallighetFastighetId) o;
        return samfallighet_id == omr.samfallighet_id &&
                fastighet_id.equals(omr.fastighet_id);
    }
}
