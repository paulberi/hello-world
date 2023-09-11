package se.metria.markkoll.entity.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.metria.markkoll.entity.samfallighet.SamfallighetMerInfoEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(schema = "config", name = "kund")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class KundEntity {
    @Id
    @NonNull
    private String id;

    @OneToMany(mappedBy = "kund")
    private List<SamfallighetMerInfoEntity> samfallighetMerInfo;
}
