package se.metria.matdatabas.service.scheduler;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.scheduler.config.ContextAwareRunnable;
import se.metria.matdatabas.service.scheduler.config.CustomRequestScopeAttr;
import se.metria.matdatabas.service.scheduler.entity.SchedulerJobEntity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static se.metria.matdatabas.db.Tables.ANVANDARE;

@Service
public class SchedulerService {

	private static final Integer SYSTEM_USER_ID = 0;

	private ApplicationContext applicationContext;
	private DSLContext dslContext;
	private SecurityContext securityContext;
	private CustomRequestScopeAttr customRequestScopeAttr;
	private SchedulerRepository schedulerRepository;
	private Scheduler scheduler;

	public SchedulerService(ApplicationContext applicationContext,
							DSLContext dslContext,
							Scheduler scheduler,
							SchedulerRepository schedulerRepository) {
		this.applicationContext = applicationContext;
		this.dslContext = dslContext;
		this.securityContext = createSchedulerSecurityContext();
		this.customRequestScopeAttr = new CustomRequestScopeAttr();
		this.schedulerRepository = schedulerRepository;
		this.scheduler = scheduler;
	}

	public void startAllSchedulers() throws SchedulerException {
		scheduler.clear();

		List<SchedulerJobEntity> jobInfoList = schedulerRepository.findAll();
		if (jobInfoList != null) {
			jobInfoList.forEach(jobInfo -> {
				scheduleNewJob(jobInfo);
			});
		}
	}

	public void scheduleNewJob(SchedulerJobEntity jobInfo) {
		scheduleNewJobHelper(jobInfo, false);
	}

	private void scheduleNewJobHelper(SchedulerJobEntity jobInfo, boolean now) {
		Job service = (Job) applicationContext.getBean(jobInfo.getJobClass());

		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(service.getClass())
				.withIdentity(jobInfo.getJobName(),jobInfo.getJobGroup())
				.build();

		Trigger trigger;

		if (now) {
			trigger = TriggerBuilder.newTrigger()
					.startNow()
					.build();
		} else if (jobInfo.getCronJob()) {
			trigger = newTrigger()
					.withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
					.withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCronExpression()))
					.build();
		} else {
			throw new IllegalArgumentException();
		}

		// Tell quartz to schedule the job using our trigger
		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			throw new RuntimeException("Scheduling error", e);
		}
	}

	public void runWithSystemUser(Runnable task) {
		ContextAwareRunnable runnable = new ContextAwareRunnable(task, customRequestScopeAttr, securityContext);

		runnable.run();
	}

	public void startJobNow(Integer id) {
		Optional<SchedulerJobEntity> jobInfo = schedulerRepository.findById(id);

		if (jobInfo.isPresent()) {
			JobKey jobKey = new JobKey(jobInfo.get().getJobName(), jobInfo.get().getJobGroup());

			try {
				if (scheduler.checkExists(jobKey)) {
					scheduler.triggerJob(jobKey);
				} else {
					startJobNow(jobInfo.get());
				}
			} catch (SchedulerException e) {
				throw new RuntimeException("Scheduling error", e);
			}

		} else {
			throw new IllegalArgumentException("job Id not found");
		}
	}

	public void startJobNow(SchedulerJobEntity jobInfo) {
		scheduleNewJobHelper(jobInfo, true);
	}

	public void jobDone(String jobServiceTyp, Short status) {
		SchedulerJobEntity schedulerJobEntity = getJobInfo(jobServiceTyp);
		if (schedulerJobEntity != null) {
			LocalDateTime dateTime = LocalDateTime.now();
			schedulerJobEntity.setLatestStatus(status);
			schedulerJobEntity.setLatestRun(dateTime);
			if (status == Jobstatus.OK) {
				schedulerJobEntity.setLatestOk(dateTime);
			}
			schedulerRepository.save(schedulerJobEntity);
		}
	}

	public LocalDateTime getLastRunOkDate(String jobServiceTyp) {
		SchedulerJobEntity schedulerJobEntity = getJobInfo(jobServiceTyp);
		if (schedulerJobEntity == null) {
			return null;
		}
		return schedulerJobEntity.getLatestOk();
	}

	private SecurityContext createSchedulerSecurityContext() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		MatdatabasUser matdatabasUser = getSystemUser();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(matdatabasUser.getUsername(),
				"", matdatabasUser.getAuthorities());
		token.setDetails(matdatabasUser);
		context.setAuthentication(token);
		return context;
	}

	private MatdatabasUser getSystemUser() {
		Record dbuser = dslContext.select(
				ANVANDARE.INLOGGNINGS_NAMN,
				ANVANDARE.ID,
				ANVANDARE.AKTIV,
				ANVANDARE.BEHORIGHET,
				ANVANDARE.NAMN,
				ANVANDARE.FORETAG,
				ANVANDARE.DEFAULT_KARTLAGER_ID)
				.from(ANVANDARE)
				.where(ANVANDARE.ID.eq(SYSTEM_USER_ID))
				.fetchOne();
		MatdatabasUser user = new MatdatabasUser(dbuser.get(ANVANDARE.INLOGGNINGS_NAMN),
				"",
				dbuser.get(ANVANDARE.AKTIV),
				true,
				true,
				true,
				MatdatabasRole.getRoles(dbuser.get(ANVANDARE.BEHORIGHET)).stream().map(SimpleGrantedAuthority::new).collect(toSet()),
				dbuser.get(ANVANDARE.NAMN),
				dbuser.get(ANVANDARE.FORETAG),
				dbuser.get(ANVANDARE.DEFAULT_KARTLAGER_ID),
				dbuser.get(ANVANDARE.ID),
				LocalDateTime.now());
		return user;
	}

	public SchedulerJobEntity getJobInfo(String jobServiceTyp) {
		return  schedulerRepository.getSchedulerJobInfoByJobClass(jobServiceTyp);
	}

	public Collection<SchedulerJobEntity> getAllJobs() {
		return schedulerRepository.findAll();
	}
}
