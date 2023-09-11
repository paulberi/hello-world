package se.metria.xplore.samrad.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import se.metria.xplore.samrad.commons.utils.Auditable;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kund")
@ToString
public class KundEntity{

    @Id
    @NonNull
    private String id;


    private String slug;


    private String namn;


    @OneToMany(cascade = CascadeType.ALL)
    private List<ProjektEntity> projektEntityList;

    //to be implemented ith an object
    private String standardSprak;


    private String bakgrund;


    private String forgrund;

    @OneToOne
    private FilEntity logotyp;

    private String tillgangligtSprak;


    @OneToMany(cascade = CascadeType.ALL)
    private List<FilEntity> bildList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AnvandareEntity> anvandareEntityList;


}