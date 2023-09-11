package se.metria.matdatabas.service.statusrapport;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.scheduler.JobService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.Jobstatus;
import se.metria.matdatabas.service.scheduler.SchedulerService;

@Component
@DisallowConcurrentExecution
public class StatusJob implements JobService {
    private Logger logger = LoggerFactory.getLogger(StatusJob.class);

    private SchedulerService schedulerService;
    private StatusService statusService;

    public StatusJob(SchedulerService schedulerService, StatusService statusService) {
        this.schedulerService = schedulerService;
        this.statusService = statusService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Status job started");

        schedulerService.runWithSystemUser(() ->{
            statusService.skickaStatusmailAllaAnvandare();
            jobDone(JobServicetyper.STATUS_MAIL, Jobstatus.OK);
        });

        logger.info("Status job done");
    }

    @Override
    public void jobDone(String jobServiceTyp, Short status) {
        schedulerService.jobDone(jobServiceTyp, status);
    }

}

