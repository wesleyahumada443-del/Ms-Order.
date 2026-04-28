package ms.order.service.domain.port.out;



public interface BaseEventsOutPort<T> {
    void sendMessage(T message);
}
