package se.metria.markkoll.service.finfo;

import java.util.UUID;

public class FinfoJobFailedException extends FinfoJobException {
    public FinfoJobFailedException(UUID jobId, String message) {
        super(jobId, message);
    }
}
