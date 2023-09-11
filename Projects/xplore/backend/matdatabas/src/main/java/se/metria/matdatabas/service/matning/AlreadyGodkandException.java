package se.metria.matdatabas.service.matning;

import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.entity.MatningEntity;

public class AlreadyGodkandException extends Exception {
    SaveMatning matningCurrent;
    MatningEntity matningPrevious;
    MatningEntity matningEntity;

    public AlreadyGodkandException(SaveMatning matningCurrent, MatningEntity matningPrevious) {
        super();
        this.matningCurrent = matningCurrent;
        this.matningPrevious = matningPrevious;
    }

    public SaveMatning getMatningCurrent() {
        return this.matningCurrent;
    }
    public MatningEntity getMatningPrevious() { return this.matningPrevious; }
}
