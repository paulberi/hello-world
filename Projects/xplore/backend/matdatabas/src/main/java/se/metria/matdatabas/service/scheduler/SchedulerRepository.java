package se.metria.matdatabas.service.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.scheduler.entity.SchedulerJobEntity;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobEntity, Integer> {

	SchedulerJobEntity getSchedulerJobInfoByJobClass(String serviceTyp);
}
