package se.metria.markkoll.entity.samfallighet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.collection.internal.PersistentList;
import se.metria.markkoll.entity.fastighet.FastighetEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "samfallighet")
@NoArgsConstructor
public class SamfallighetEntity {
    @Id
    @Column(name = "fastighet_id")
    private UUID id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private FastighetEntity fastighet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "samfallighet", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SamfallighetIngaendeFastighetEntity> delagandeFastigheter = new ArrayList<>();

    @OneToMany(mappedBy = "samfallighet", cascade = CascadeType.ALL)
    private List<SamfallighetMerInfoEntity> samfallighetMerInfo = new ArrayList<>();

    public void setDelagandeFastigheter(List<SamfallighetIngaendeFastighetEntity> samfallighetFastighet) {
        if (samfallighetFastighet instanceof PersistentList) {
            this.delagandeFastigheter = samfallighetFastighet;
        }
        else {
            this.delagandeFastigheter.forEach(sf -> sf.setSamfallighet(null));
            this.delagandeFastigheter.clear();
            this.delagandeFastigheter.addAll(samfallighetFastighet);
        }
    }

    public void addDelagandeFastighet(SamfallighetIngaendeFastighetEntity ingaendeFastighet) {
        delagandeFastigheter.add(ingaendeFastighet);
        ingaendeFastighet.setSamfallighet(this);
    }

    public void setFastighet(FastighetEntity fastighet) {
        this.fastighet = fastighet;
        this.id = fastighet.getId();
    }
}