package se.metria.xplore.kundconfig.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserEntity{
    @Id
    String id;

    @Column(name = "firstname")
    String firstName;

    @Column(name = "lastname")
    String lastName;

    String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    Set<PermissionsEntity> permissions = new HashSet<>();
}
