package se.metria.matdatabas.service.inrapportering;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.scheduler.JobService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.SchedulerService;

import java.time.LocalDateTime;

@Component
@DisallowConcurrentExecution
public class ImportVattenstandJob implements JobService {
    private Logger logger = LoggerFactory.getLogger(ImportVattenstandJob.class);

    private SchedulerService schedulerService;
    private ImportVattenstandService importVattenstandService;

    public ImportVattenstandJob(SchedulerService schedulerService, ImportVattenstandService importVattenstandService) {
        this.schedulerService = schedulerService;
        this.importVattenstandService = importVattenstandService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Vattenstand import job started");

        LocalDateTime fromDate = schedulerService.getLastRunOkDate(JobServicetyper.IMPORT_VATTENSTAND);
        if (fromDate == null) {
            fromDate = LocalDateTime.now().minusDays(1);
        }
        LocalDateTime toDate = LocalDateTime.now();

        LocalDateTime finalFromDate = fromDate;

        schedulerService.runWithSystemUser(() ->{
            Short status = importVattenstandService.importVattenstand(finalFromDate, toDate);
            jobDone(JobServicetyper.IMPORT_VATTENSTAND, status);
        });

        logger.info("Vattenstand import job done");
    }

    @Override
    public void jobDone(String jobServiceTyp, Short status) {
        schedulerService.jobDone(jobServiceTyp, status);
    }
}

