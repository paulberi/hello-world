package se.metria.markkoll.entity.fastighet;

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
public class OmradeId implements Serializable {
    UUID fastighetId;
    long omradeNr;
}
