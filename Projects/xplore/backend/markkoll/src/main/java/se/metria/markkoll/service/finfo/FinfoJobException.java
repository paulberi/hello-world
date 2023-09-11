package se.metria.markkoll.service.finfo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FinfoJobException extends Exception {
    protected UUID jobId;

    public FinfoJobException(UUID jobId, String message) {
        super(message);
        this.jobId = jobId;
    }
}
