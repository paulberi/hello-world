package se.metria.xplore.fme;

import org.springframework.http.HttpStatus;

public class FmeException extends RuntimeException{
    private HttpStatus errorCode;

    public FmeException(HttpStatus errorCode, FmeError fmeError) {
        super(fmeError.name());
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return this.errorCode;
    }
}
