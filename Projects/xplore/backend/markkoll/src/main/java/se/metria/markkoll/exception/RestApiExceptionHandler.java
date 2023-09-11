package se.metria.markkoll.exception;

import io.opentracing.Span;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Hantera MarkkollException. Returnerar statuskod 400 Bad Request
     * med ett felmeddelande som visas för användaren i frontend.
     */
    @ExceptionHandler(MarkkollException.class)
    public ResponseEntity<?> handleMarkkollException(MarkkollException ex, WebRequest request)
    {
        var status = HttpStatus.BAD_REQUEST;
        var body = errorBody(request, status, ex.getErrorName(), ex.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Hantera uppladdning av för stora filer.
     * Returnera statuskod 400 Bad Request.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?>handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, WebRequest request)
    {
        var status = HttpStatus.BAD_REQUEST;
        var body = errorBody(request, status, status.getReasonPhrase(), MarkkollError.UPLOAD_ERROR_MAX_FILE_SIZE.getMessage());

        return new ResponseEntity<>(body, status);
    }

    /**
     * Hantera om användaren saknar eller har felaktiga inloggningsuppgifter.
     * Returnera statuskod 401 Unauthorized.
     */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {

        var status = HttpStatus.UNAUTHORIZED;
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        return new ResponseEntity(body, status);
    }

    /**
     * Hantera om metodargument annoterade med @Valid inte är giltiga.
     * Returnera statuskod 400 Bad Request.
     */
    @Override
    @NonNull
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                 @NonNull HttpHeaders headers,
                                 @NonNull HttpStatus status,
                                 @NonNull WebRequest request)
    {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var body = errorBody(request, httpStatus, httpStatus.getReasonPhrase(), MarkkollError.FORM_ERROR.getMessage());

        return new ResponseEntity(body, status);
    }

    /**
     * Hantera fel vid http-anrop till integrationer med Webflux.
     * Returnera statuskod 503 Service Unavailable.
     */
    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<?> handleWebClientRequestException(WebClientRequestException ex, WebRequest request)
    {
        // Logga fel med integrationer
        log.error(ex.getMessage(), ex);

        var status = HttpStatus.SERVICE_UNAVAILABLE;
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        logApmError(ex);

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, WebRequest request)
    {
        // Logga fel med integrationer
        log.error(ex.getMessage(), ex);

        var status = ex.getStatus();
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        logApmError(ex);

        return new ResponseEntity<>(body, status);
    }

    /**
     * Hantera alla övriga fel.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request)
    {
        log.error(ex.getMessage(), ex);

        var status = statusCode(ex);
        var body = errorBody(request, status, status.getReasonPhrase(), ex.getMessage());

        logApmError(ex);

        return new ResponseEntity<>(body, status);
    }

    private void logApmError(Exception ex) {
        if (GlobalTracer.isRegistered()) {
            Span span = GlobalTracer.get().activeSpan();
            if (span != null) {
                var stackTrace = new StringWriter();
                ex.printStackTrace(new PrintWriter(stackTrace));
                span.setTag("error", true);
                span.log(Map.of("Event", "error", "error-object", ex, "stack", stackTrace));
            }
        }
    }

    /**
     * Ta reda på statuskod från annotering med ResponseStatus, annars sätt Internal Server Error.
     */
    private HttpStatus statusCode(Exception ex) {
        ResponseStatus annotation = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

        return annotation != null ? annotation.code() : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * Bygg upp errorobjektet som skickas till frontend.
     */
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



}
