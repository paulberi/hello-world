package se.metria.finfo.entity.registerenhet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "registerenhet", schema= "finfo")
public class RegisterenhetEntity extends BaseEntity<UUID> {

    private String andamal;
    private String forvaltandeBeteckning;
    private UUID uuid;

    @CreatedDate
    private LocalDateTime importeradDatum;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "registerenhet")
    private List<PagaendeFastighetsbildningEntity> pagaendeFastighetsbildning = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "registerenhet")
    private List<RattighetEntity> rattighet = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "registerenhet")
    private List<BerordAvAtgardEntity> berordAvAtgard = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private RegisterenhetTyp typ;

    public void addPagaendeFastighetsbildning(PagaendeFastighetsbildningEntity pagaendeFastighetsbildning) {
        pagaendeFastighetsbildning.setRegisterenhet(this);
        this.pagaendeFastighetsbildning.add(pagaendeFastighetsbildning);
    }

    public void addRattighet(RattighetEntity rattighet) {
        rattighet.setRegisterenhet(this);
        this.rattighet.add(rattighet);
    }

    public void addBerordAvAtgard(BerordAvAtgardEntity berordAvAtgard) {
        berordAvAtgard.setRegisterenhet(this);
        this.berordAvAtgard.add(berordAvAtgard);
    }
}
