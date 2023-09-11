package se.metria.xplore.fme;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketTimeoutException;

@ControllerAdvice
public class FmeExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {FmeException.class})
    protected ResponseEntity<Object> handleFmeException(FmeException ex, WebRequest request) {
        return new ResponseEntity(ex.getMessage(), new HttpHeaders(), ex.getErrorCode());
    }

    @ExceptionHandler(value = {ResourceAccessException.class})
    protected ResponseEntity<Object> handleResourceAccessException(ResourceAccessException ex, WebRequest request) {
        if (ex.getMostSpecificCause() instanceof SocketTimeoutException)
        {
            return new ResponseEntity(FmeError.TIMEOUT.name(), new HttpHeaders(), HttpStatus.REQUEST_TIMEOUT);
        }
        else
        {
            return new ResponseEntity(FmeError.UNKNOWN_ERROR.name(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return new ResponseEntity(FmeError.UNKNOWN_ERROR.name(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}