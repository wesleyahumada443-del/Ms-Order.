package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Order;
public interface CreateOrderInPort {
    Order createOrder(Order order);
}
