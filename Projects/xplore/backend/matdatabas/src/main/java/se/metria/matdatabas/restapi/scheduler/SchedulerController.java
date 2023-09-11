package se.metria.matdatabas.restapi.scheduler;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.SchedulerApi;
import se.metria.matdatabas.openapi.model.SchedulerJobDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.scheduler.SchedulerService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequestMapping(value = "/api")
@RestController
public class SchedulerController implements SchedulerApi {
    private SchedulerService schedulerService;
    private ModelMapper mapper;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;

        mapper = new ModelMapper();
    }

    @Override
    @RolesAllowed(MatdatabasRole.ADMINISTRATOR)
    public ResponseEntity<List<SchedulerJobDto>> schedulerJobsGet() {
        return ResponseEntity.ok(mapper.map(schedulerService.getAllJobs(),
                new TypeToken<List<SchedulerJobDto>>(){}.getType()));
    }

    @Override
    public ResponseEntity<Void> schedulerStartJobPost(@NotNull @Valid Integer id) {
        schedulerService.startJobNow(id);

        return ResponseEntity.noContent().build();
    }
}
