package se.metria.markkoll.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * MarkkollException används för att meddela användaren om att ett fel har uppstått
 * pga felaktigt inmatat data. Returnerar statuskod 400 Bad Request.
 */

@Slf4j
public class MarkkollException extends RuntimeException {

    private String errorName = "";

    public MarkkollException() {
        super();
    }

    /**
     * Kasta fel med ett meddelande utan att det loggas.
     */
    public MarkkollException(String message) {
        super(message);
    }

    /**
     * Kasta fel med ett meddelande och logga det som error.
     */
    public MarkkollException(String message, Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }

    /**
     * Kasta fel med ett MarkkollError utan att det loggas.
     */
    public MarkkollException(MarkkollError error) {
        super(error.getMessage());
        this.errorName = error.getName();
    }

    /**
     * Kasta fel med ett MarkkollError och logga det som error.
     */
    public MarkkollException(MarkkollError error, Throwable cause) {
        super(error.getMessage(), cause);
        this.errorName = error.getName();
        log.error(error.toString(), cause);
    }

    /**
     * Returnera namn på fel.
     */
    public String getErrorName() {
        return this.errorName;
    }
}
