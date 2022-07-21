package jp.keio.acds.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
public class InternalError extends RuntimeException {
    private static final String defaultMessage = "Internal Server Error";
    public InternalError() {
        super(defaultMessage);
    }

    public InternalError(String message) {
        super(message);
    }

    public InternalError(Throwable e) {
        super(defaultMessage, e);
    }

    public InternalError(String message, Throwable e) {
        super(message, e);
    }
}
