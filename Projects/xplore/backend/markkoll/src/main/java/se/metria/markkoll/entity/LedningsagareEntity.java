package se.metria.markkoll.entity;

import lombok.*;
import se.metria.markkoll.entity.admin.KundEntity;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "ledningsagare")
@RequiredArgsConstructor
@NoArgsConstructor
public class LedningsagareEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String namn;

    @NonNull
    @ManyToOne
    @EqualsAndHashCode.Exclude
    private KundEntity kund;
}
