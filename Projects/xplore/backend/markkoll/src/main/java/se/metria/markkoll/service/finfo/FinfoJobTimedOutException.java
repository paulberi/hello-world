package se.metria.markkoll.service.finfo;

import java.util.UUID;

public class FinfoJobTimedOutException extends FinfoJobException {
    public FinfoJobTimedOutException(UUID jobId, String message) {
        super(jobId, message);
    }
}
