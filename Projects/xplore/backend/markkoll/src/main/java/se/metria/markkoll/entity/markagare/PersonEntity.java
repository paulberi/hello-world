package se.metria.markkoll.entity.markagare;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "person")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "markagare")
@ToString(exclude = "markagare")
public class PersonEntity {
    @Id
    @Column(insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String namn;

    private String adress = "";

    private String postnummer = "";

    private String postort = "";

    private String telefon;

    private String bankkonto;

    @OneToMany(mappedBy = "person")
    @Builder.Default
    private Set<MarkagareEntity> markagare = new HashSet<>();

    @Column(name = "e_post")
    private String ePost;

    private String personnummer;

    private String kundId;

    private String coAdress;

    private String land;

    public void addMarkagare(MarkagareEntity markagare) {
        this.markagare.add(markagare);
        markagare.setPerson(this);
    }
}
