package ms.order.service.domain.exceptions;


/**
 * The NotFoundException class is a custom RuntimeException that indicates that an entity or resource was not found.
 */
public class OrderException extends RuntimeException{

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
