package se.metria.matdatabas.service.inrapportering;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.scheduler.JobService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.SchedulerService;

import java.time.LocalDateTime;

@Component
@DisallowConcurrentExecution
public class ImportKolibriJob implements JobService {
    private Logger logger = LoggerFactory.getLogger(ImportKolibriJob.class);

    private SchedulerService schedulerService;
    private ImportKolibriService importKolibriService;

    public ImportKolibriJob(SchedulerService schedulerService, ImportKolibriService importKolibriService) {
        this.schedulerService = schedulerService;
        this.importKolibriService = importKolibriService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("Kolibri import job started");

        LocalDateTime fromDate = schedulerService.getLastRunOkDate(JobServicetyper.IMPORT_KOLIBRI);
        if (fromDate == null) {
            fromDate = LocalDateTime.now().minusDays(1);
        }
        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime finalFromDate = fromDate.minusDays(2);

        schedulerService.runWithSystemUser(() ->{
            Short status = importKolibriService.importKolibri(finalFromDate, toDate);
            jobDone(JobServicetyper.IMPORT_KOLIBRI, status);
        });

        logger.info("Kolibri import job done");
    }

    @Override
    public void jobDone(String jobServiceTyp, Short status) {
        schedulerService.jobDone(jobServiceTyp, status);
    }
}
