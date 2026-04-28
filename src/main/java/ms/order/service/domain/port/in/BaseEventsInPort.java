package ms.order.service.domain.port.in;



public interface BaseEventsInPort<T> {
    void send(T message);
}
