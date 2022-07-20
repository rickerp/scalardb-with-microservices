package jp.keio.acds.orderservice.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
