package ms.order.service.domain.exceptions;


/**
 * The NotFoundException class is a custom RuntimeException that indicates that an entity or resource was not found.
 */
public class OrderNotFoundException extends OrderException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
