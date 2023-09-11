package se.metria.markkoll.entity.vardering.fiber;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "vardering_config")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiberVarderingConfigEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private int markskapSkog;

    private int markskapJordbruksimpediment;

    private int markskapOvrigMark;

    private int optobrunnSkog;

    private int optobrunnJordbruksimpediment;

    private int optobrunnOvrigMark;

    @Column(name = "teknikbod_skog_6x6m")
    private int teknikbodSkog6x6m;

    @Column(name = "teknikbod_skog_8x8m")
    private int teknikbodSkog8x8m;

    @Column(name = "teknikbod_skog_10x10m")
    private int teknikbodSkog10x10m;

    @Column(name = "teknikbod_jordbruksimpediment_6x6m")
    private int teknikbodJordbruksimpediment6x6m;

    @Column(name = "teknikbod_jordbruksimpediment_8x8m")
    private int teknikbodJordbruksimpediment8x8m;

    @Column(name = "teknikbod_jordbruksimpediment_10x10m")
    private int teknikbodJordbruksimpediment10x10m;

    @Column(name = "teknikbod_ovrig_mark_6x6m")
    private int teknikbodOvrigMark6x6m;

    @Column(name = "teknikbod_ovrig_mark_8x8m")
    private int teknikbodOvrigMark8x8m;

    @Column(name = "teknikbod_ovrig_mark_10x10m")
    private int teknikbodOvrigMark10x10m;

    @Column(name = "schablonersattning_optoror_1m")
    private double schablonersattningOptoror1m;

    @Column(name = "schablonersattning_optoror_2m")
    private double schablonersattningOptoror2m;

    private int grundersattning;

    private int minimiersattning;

    private int tillaggExpropriationslagen;

    private int sarskildErsattning;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OneToMany(mappedBy = "varderingConfig")
    private List<VarderingKundAgareEntity> varderingKundAgare;

    private boolean minimiersattningEnbartMarkledning;

    private int sarskildErsattningMaxbelopp;
}
