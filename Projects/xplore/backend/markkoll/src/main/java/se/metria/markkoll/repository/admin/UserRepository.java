package se.metria.markkoll.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.admin.RoleEntity;
import se.metria.markkoll.entity.admin.UserEntity;
import se.metria.markkoll.openapi.model.RoleTypeDto;

import java.util.Collection;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    @Query("SELECT u.kund FROM UserEntity u WHERE u.id = :userId")
    String getKundId(String userId);

    @Query("SELECT u.roles FROM UserEntity u WHERE u.id = :userId")
    Set<RoleEntity> getRoles(String userId);

    @Query("SELECT u FROM UserEntity u WHERE u.kund.id = :kundId")
    Set<UserEntity> getUsersForKund(String kundId);

    @Query("SELECT u " +
        "     FROM UserEntity u " +
        "     JOIN u.roles r " +
        "    WHERE r.objectId = :objectId " +
        "      AND r.roleType IN :roleTypeDtos")
    Set<UserEntity> getUsersForRoles(String objectId, Collection<RoleTypeDto> roleTypeDtos);

    @Query("SELECT u " +
            "     FROM UserEntity u " +
            "     JOIN u.roles r " +
            "    WHERE r.objectId = :kundId " +
            "      AND r.roleType = :roleType")
    Set<UserEntity> getUserForKundByRoleType(String kundId, RoleTypeDto roleType);

    @Query("SELECT u.id " +
        "     FROM UserEntity u " +
        "     JOIN u.roles r " +
        "    WHERE r.objectId = :objectId " +
        "      AND r.roleType IN :roleTypeDtos")
    Set<String> getUserIdsForRoles(String objectId, Collection<RoleTypeDto> roleTypeDtos);

    @Query("SELECT u.id FROM UserEntity u")
    Set<String> getAllIds();
}
