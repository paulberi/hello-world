package se.metria.markkoll.entity.samfallighet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagaendeFastighetsbildning {
    private String arendestatus;
    private String dagboksnummer;
}
