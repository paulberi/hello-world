package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "sprak")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class SprakEntity {

    @Id
    private String kod;

    @NonNull
    private String namn;

    @NonNull
    @Column(name = "namn_org")
    private String namnOrg;

    @Override
    public String toString() {
        return kod;
    }
}
