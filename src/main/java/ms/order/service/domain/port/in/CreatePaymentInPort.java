package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Payment;

public interface CreatePaymentInPort {

    Payment createPayment(Payment payment);
}
