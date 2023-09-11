package se.metria.markkoll.entity.admin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(schema = "config", name = "markkoll_user")
@RequiredArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String efternamn;

    @NonNull
    private String email;

    @NonNull
    private String fornamn;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    private KundEntity kund;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user", orphanRemoval = true)
    private Set<RoleEntity> roles = new HashSet<>();

    public boolean addRole(RoleEntity roleEntity) {
        roleEntity.setUser(this);
        return roles.add(roleEntity);
    }

    public boolean hasRole(RoleTypeDto roleType, String objectId) {
        return roles.contains(new RoleEntity(objectId, roleType, this));
    }

    public boolean removeRole(RoleEntity roleEntity) {
        roleEntity.setUser(null);
        return roles.remove(roleEntity);
    }
}
