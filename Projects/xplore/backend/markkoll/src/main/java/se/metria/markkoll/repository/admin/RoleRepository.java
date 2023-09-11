package se.metria.markkoll.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import se.metria.markkoll.entity.admin.RoleEntity;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
}
