package jp.keio.acds.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not found")
public class NotFound extends RuntimeException {
    private static final String defaultMessage = "Not found";
    public NotFound() {
        super(defaultMessage);
    }

    public NotFound(String message) {
        super(message);
    }

    public NotFound(Throwable e) {
        super(defaultMessage, e);
    }

    public NotFound(String message, Throwable e) {
        super(message, e);
    }
}
