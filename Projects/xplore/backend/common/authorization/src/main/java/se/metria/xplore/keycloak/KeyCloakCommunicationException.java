package se.metria.xplore.keycloak;

public class KeyCloakCommunicationException extends RuntimeException {
    public KeyCloakCommunicationException() {
        super();
    }

    public KeyCloakCommunicationException(String message) {
        super(message);
    }

    public KeyCloakCommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyCloakCommunicationException(Throwable cause) {
        super(cause);
    }

    protected KeyCloakCommunicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
