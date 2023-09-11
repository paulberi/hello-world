package se.metria.xplore.kundconfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.xplore.kundconfig.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity getFirstById(String id);
    void deleteById(String id);

    @Query("SELECT DISTINCT u " +
            "FROM UserEntity u " +
            "JOIN PermissionsEntity p " +
              "on p.user.id = u.id " +
            "JOIN KundEntity k " +
              "ON k.id = p.kund.id " +
           "WHERE k.id = :kundId")
    List<UserEntity> getUsersForKund(String kundId);
}
