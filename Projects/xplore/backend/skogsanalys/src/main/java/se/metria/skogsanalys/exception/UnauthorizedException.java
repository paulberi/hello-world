package se.metria.skogsanalys.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Inloggningsuppgifter saknas eller är felaktiga. Returnerar statuskod 401.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super();
    }
}
