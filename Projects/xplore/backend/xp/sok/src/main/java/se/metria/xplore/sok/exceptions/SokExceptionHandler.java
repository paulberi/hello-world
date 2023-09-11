package se.metria.xplore.sok.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SokExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InternalGeofilterException.class})
    protected ResponseEntity<Object> handleInternalGeofilterException(InternalGeofilterException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(value = {ExternalGeofilterException.class})
    protected ResponseEntity<Object> handleExternalGeofilterException(ExternalGeofilterException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {GmlConversionException.class})
    protected ResponseEntity<Object> handleGmlConversionException(GmlConversionException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {GeoJsonMergeException.class})
    protected ResponseEntity<Object> handleGeoJsonMergeException(GeoJsonMergeException ex, WebRequest request) {
        System.out.println(ex.toString());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}