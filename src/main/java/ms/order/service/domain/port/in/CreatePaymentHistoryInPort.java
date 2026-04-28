package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Payment;
import ms.order.service.domain.model.PaymentEvent;

public interface CreatePaymentHistoryInPort {
    void createPaymentHistory(PaymentEvent paymentEvent);
    void createPaymentHistory(Payment payment);
}
