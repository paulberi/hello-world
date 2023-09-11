package se.metria.matdatabas.service.inrapportering;

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
public class ImportSMHIJob implements JobService {
    private Logger logger = LoggerFactory.getLogger(ImportSMHIJob.class);

    private SchedulerService schedulerService;
    private ImportSMHIService importSMHIService;

    public ImportSMHIJob(SchedulerService schedulerService, ImportSMHIService importSMHIService) {
        this.schedulerService = schedulerService;
        this.importSMHIService = importSMHIService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("SMHI import job started");

        schedulerService.runWithSystemUser(() ->{
            importSMHIService.importSMHI();
            jobDone(JobServicetyper.IMPORT_SMHI, Jobstatus.OK);
        });
        logger.info("SMHI import job done");
    }

    @Override
    public void jobDone(String jobServiceTyp, Short status) {
        schedulerService.jobDone(jobServiceTyp, status);
    }
}
