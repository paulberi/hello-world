package se.metria.finfo.entity.importjob;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.openapi.model.ImportJobStatusDto;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "import_job", schema= "finfo")
@ToString
public class ImportJobEntity extends BaseEntity <UUID> {
    @Enumerated(EnumType.STRING)
    private ImportJobStatusDto status = ImportJobStatusDto.CREATED;

    private String resource;
    private String type;
}
