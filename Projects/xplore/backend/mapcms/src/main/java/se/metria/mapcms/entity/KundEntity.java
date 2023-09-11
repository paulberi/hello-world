package se.metria.mapcms.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "kund")
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class KundEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    @Column(name = "vht_nyckel")
    private String vhtNyckel;

    @NonNull
    private String slug;

    @NonNull
    private String namn;

    @Type(type = "jsonb-node")
    @Column(columnDefinition = "jsonb")
    private JsonNode egenskaper = JsonNodeFactory.instance.objectNode();

    @NonNull
    @OneToOne
    @JoinColumn(name = "standardsprak")
    private SprakEntity standardsprak;

    @OneToOne
    @JoinColumn(name = "logotyp")
    private FilEntity logotyp;

    @NonNull
    @OneToMany
    @JoinTable(name = "kund_sprak",
            joinColumns = { @JoinColumn(name = "kund_id") },
            inverseJoinColumns = { @JoinColumn(name = "sprak_kod") })
    private List<SprakEntity> tillgangligaSprak;

    @OneToMany(mappedBy = "kund", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjektEntity> projekt;

    public void addProjekt(ProjektEntity projekt) {
        projekt.setKund(this);
        this.projekt.add(projekt);
    }
}
