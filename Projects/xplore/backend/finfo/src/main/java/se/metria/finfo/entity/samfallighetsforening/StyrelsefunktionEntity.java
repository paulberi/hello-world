package se.metria.finfo.entity.samfallighetsforening;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "styrelsefunktion", schema= "finfo")
public class StyrelsefunktionEntity extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StyrelsemedlemEntity styrelsemedlem;

    private String styrelsefunktionTyp;
}
