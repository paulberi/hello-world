package se.metria.matdatabas.service.scheduler;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.JobBuilder.*;


/**
 * Schedules all jobs defined in db
 */
@Component
public class SchedulerStartUpHandler implements ApplicationRunner {
	private SchedulerService schedulerService;
	private ApplicationContext applicationContext;
	private Scheduler scheduler;

	public SchedulerStartUpHandler(SchedulerService schedulerService,
								   Scheduler scheduler,
								   ApplicationContext applicationContext) {
		this.schedulerService = schedulerService;
		this.applicationContext = applicationContext;
		this.scheduler = scheduler;
	}

	@Override
	public void run(ApplicationArguments args) {
		try {
			schedulerService.startAllSchedulers();
		} catch (Exception ex) {
			// Log this
		}
	}
}
