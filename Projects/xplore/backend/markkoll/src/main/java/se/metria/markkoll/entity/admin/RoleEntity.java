package se.metria.markkoll.entity.admin;

import lombok.*;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@Table(schema = "config", name = "user_role")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String objectId;

    @NonNull
    private RoleTypeDto roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private UserEntity user;

    public RoleEntity(String objectId, RoleTypeDto roleType, UserEntity user) {
        this.objectId = objectId;
        this.roleType = roleType;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;

        return objectId.equals(that.objectId) &&
            roleType == that.roleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, roleType);
    }
}
