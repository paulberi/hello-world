package se.metria.mapcms.commons.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

/**
 * Från Markkoll
 * **/

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     *
     * Hantera MapcmsException. Returnerar statuskod 404 NOT FOUND
     * med ett felmeddelande som visas för användaren i frontend.
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleMapCMSException(EntityNotFoundException ex, WebRequest request)
    {
        var status = HttpStatus.NOT_FOUND;
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(body, headers, status);
    }

    private Map<String, Object> errorBody(WebRequest request, HttpStatus status, String error, String message) {
        var opts = ErrorAttributeOptions.defaults().including(
                ErrorAttributeOptions.Include.MESSAGE
        );

        var body = new DefaultErrorAttributes().getErrorAttributes(request, opts);

        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return body;
    }

    /**
     *
     * Hantera MapcmsException. Returnerar statuskod 400 Bad Request
     * med ett felmeddelande som visas för användaren i frontend.
     */
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> handleAnyBadRequest(Exception ex, WebRequest request)
    {
        var status = HttpStatus.BAD_REQUEST;
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity<>(body, status);
    }

}
