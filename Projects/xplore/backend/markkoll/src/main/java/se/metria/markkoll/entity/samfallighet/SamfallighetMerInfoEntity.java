package se.metria.markkoll.entity.samfallighet;

import lombok.Getter;
import lombok.Setter;
import se.metria.markkoll.entity.admin.KundEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "samfallighet_mer_info")
public class SamfallighetMerInfoEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private KundEntity kund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "samf_id", nullable = false)
    private SamfallighetEntity samfallighet;

    @ElementCollection
    @CollectionTable(name = "atgard", joinColumns = @JoinColumn(name = "samf_mer_info_id"))
    private List<BerordAvAtgard> berordAvAtgard = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "pagaende_fastighetsbildning", joinColumns = @JoinColumn(name = "samf_mer_info_id"))
    private List<PagaendeFastighetsbildning> pagaendeFastighetsbildning = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "rattighet", joinColumns = @JoinColumn(name = "samf_mer_info_id"))
    private List<Rattighet> rattighet = new ArrayList<>();

    private String firmatecknare;
    private String coNamn;
    private String coAdress;
    private String coPostnummer;
    private String coPostort;
    private String forvaltandeBeteckning;
    private String andamal;
    private String foreningstyp;
    private String organisationsnummer;
}
