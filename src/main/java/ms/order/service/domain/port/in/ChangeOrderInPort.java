package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.PaymentEvent;

public interface ChangeOrderInPort {
    Order updateById(Order order);
    void changePaymentOrder(PaymentEvent paymentEvent);
}
