package ms.order.service.domain.exceptions;

public class OrderStatusInvalidException extends RuntimeException {

    public OrderStatusInvalidException(String message) {
        super(message);
    }
}
