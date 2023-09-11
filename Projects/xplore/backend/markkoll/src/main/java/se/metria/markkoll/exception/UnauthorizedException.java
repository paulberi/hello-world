package se.metria.markkoll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.metria.markkoll.exception.MarkkollException;

/**
 * Inloggningsuppgifter saknas eller är felaktiga. Returnerar statuskod 401.
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends MarkkollException {
    public UnauthorizedException() {
        super();
    }
}
