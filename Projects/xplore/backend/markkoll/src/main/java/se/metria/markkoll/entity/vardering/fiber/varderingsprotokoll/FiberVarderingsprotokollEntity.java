package se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll;

import lombok.*;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(schema = "fiber", name = "varderingsprotokoll")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FiberVarderingsprotokollEntity implements Serializable {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    AvtalEntity avtal;

    @Embedded
    @Builder.Default
    FiberVarderingsprotokollMetadataEntity metadata = new FiberVarderingsprotokollMetadataEntity();

    @Embedded
    @Builder.Default
    FiberVarderingsprotokollConfigEntity config = new FiberVarderingsprotokollConfigEntity();
    
    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<FiberPunktersattningEntity> punktersattning = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<FiberMarkledningEntity> markledning = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<FiberIntrangAkerOchSkogsmarkEntity> intrangAkerOchSkogsmark = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<FiberOvrigIntrangsersattningEntity> ovrigIntrangsersattning = new ArrayList<>();

    public void addMarkledning(FiberMarkledningEntity entity) {
        markledning.add(entity);
        entity.setVarderingsprotokoll(this);
    }

    public void addIntrangAkerOchSkogsmark(FiberIntrangAkerOchSkogsmarkEntity entity) {
        intrangAkerOchSkogsmark.add(entity);
        entity.setVarderingsprotokoll(this);
    }

    public void addOvrigIntrangsersattning(FiberOvrigIntrangsersattningEntity entity) {
        ovrigIntrangsersattning.add(entity);
        entity.setVarderingsprotokoll(this);
    }

    public void addPunktersattning(FiberPunktersattningEntity entity) {
        punktersattning.add(entity);
        entity.setVarderingsprotokoll(this);
    }
}
