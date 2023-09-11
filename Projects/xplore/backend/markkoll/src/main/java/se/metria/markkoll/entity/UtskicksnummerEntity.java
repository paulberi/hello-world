package se.metria.markkoll.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "utskicksnummer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtskicksnummerEntity {
    @Id
    String kundId;

    @Builder.Default
    Integer utskicksnummer = 1;
}
