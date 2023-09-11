package se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll;

import lombok.*;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.vardering.bilaga.BilagaEntity;
import se.metria.markkoll.entity.vardering.elnat.*;
import se.metria.markkoll.openapi.model.ElnatPrisomradeDto;
import se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto;
import se.metria.markkoll.openapi.model.ElnatZonDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(schema = "elnat", name = "varderingsprotokoll")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatVarderingsprotokollEntity implements Serializable {
    @Id
    @GeneratedValue
    UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    AvtalEntity avtal;

    @Embedded
    @Builder.Default
    ElnatVarderingsprotokollMetadataEntity metadata = new ElnatVarderingsprotokollMetadataEntity();

    @Embedded
    @Builder.Default
    ElnatVarderingsprotokollConfigEntity config = new ElnatVarderingsprotokollConfigEntity();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatPunktersattningEntity> punktersattning = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatLedningSkogsmarkEntity> ledningSkogsmark = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatMarkledningEntity> markledning = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatSsbSkogsmarkEntity> ssbSkogsmark = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatSsbVaganlaggningEntity> ssbVaganlaggning = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatOvrigtIntrangEntity> ovrigtIntrang = new ArrayList<>();

    @OneToMany(mappedBy = "varderingsprotokoll", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ElnatHinderAkermarkEntity> hinderAkermark = new ArrayList<>();

    Double rotnetto;

    @OneToMany(mappedBy = "varderingsprotokoll", orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<BilagaEntity> bilagor = new HashSet<>();

    ElnatPrisomradeDto prisomrade;

    public void addPunktersattning(String beskrivning, int antal, ElnatPunktersattningTypDto typ) {
        var entity = new ElnatPunktersattningEntity(beskrivning, antal, typ);

        entity.setVarderingsprotokoll(this);
        punktersattning.add(entity);
    }

    public void addLedningSkogsmark(String beskrivning, int ersattning) {
        var entity = new ElnatLedningSkogsmarkEntity(beskrivning, ersattning);

        entity.setVarderingsprotokoll(this);
        ledningSkogsmark.add(entity);
    }

    public void addMarkledning(String beskrivning, int langd, int bredd) {
        var entity = new ElnatMarkledningEntity(beskrivning, langd, bredd);

        entity.setVarderingsprotokoll(this);
        markledning.add(entity);
    }

    public void addSsbSkogsmark(String beskrivning, int langd, int bredd) {
        var entity = new ElnatSsbSkogsmarkEntity(beskrivning, langd, bredd);

        entity.setVarderingsprotokoll(this);
        ssbSkogsmark.add(entity);
    }

    public void addSsbVaganlaggning(String beskrivning, int langd, ElnatZonDto zon) {
        var entity = new ElnatSsbVaganlaggningEntity(beskrivning, langd, zon);

        entity.setVarderingsprotokoll(this);
        ssbVaganlaggning.add(entity);
    }

    public void addOvrigtIntrang(String beskrivning, int ersattning) {
        var entity = new ElnatOvrigtIntrangEntity(beskrivning, ersattning);

        entity.setVarderingsprotokoll(this);
        ovrigtIntrang.add(entity);
    }

    public void addHinderAkermark(String beskrivning, int ersattning) {
        var entity = new ElnatHinderAkermarkEntity(beskrivning, ersattning);

        entity.setVarderingsprotokoll(this);
        hinderAkermark.add(entity);
    }

    @PrePersist
    protected void onCreate() {
        if (prisomrade == null) { prisomrade = ElnatPrisomradeDto.NORRLANDS_INLAND; }
    }
}
