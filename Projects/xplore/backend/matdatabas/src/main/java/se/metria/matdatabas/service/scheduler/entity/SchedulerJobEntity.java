package se.metria.matdatabas.service.scheduler.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "scheduler_job_info")
@SequenceGenerator(name = "scheduler_job_info_seq", sequenceName = "scheduler_job_info_seq", allocationSize = 1)
public class SchedulerJobEntity {

	@Id
	@GeneratedValue(generator = "scheduler_job_info_seq")
	private Integer id;
	private String jobName;
	private String jobGroup;
	private String jobClass;
	private String cronExpression;
	private Long repeatTime;
	private Boolean cronJob;
	private LocalDateTime latestOk;
	private LocalDateTime latestRun;
	private Short latestStatus;

}
