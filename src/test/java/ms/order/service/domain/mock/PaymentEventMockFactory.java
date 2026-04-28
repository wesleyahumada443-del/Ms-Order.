package ms.order.service.domain.mock;

import ms.order.service.domain.model.PaymentEvent;

import java.math.BigDecimal;
import java.util.UUID;


public class PaymentEventMockFactory {

    private PaymentEvent paymentEvent;

    public PaymentEventMockFactory() {
        getPaymentEvent();
    }

    public PaymentEventMockFactory getPaymentEvent() {
        this.paymentEvent = PaymentEvent.builder()
                .orderId(UUID.randomUUID())
                .description("TEST")
                .amount(BigDecimal.valueOf(1300))
                .status("PAID")
                .build();
        return this;
    }

    public PaymentEvent build() {
        return this.paymentEvent;
    }


    public PaymentEventMockFactory ofId(UUID id) {
        this.paymentEvent.setId(id);
        return this;
    }

    public PaymentEventMockFactory ofOrderId(UUID orderId) {
        this.paymentEvent.setOrderId(orderId);
        return this;
    }

    public PaymentEventMockFactory ofAmount(BigDecimal amount) {
        this.paymentEvent.setAmount(amount);
        return this;
    }

    public PaymentEventMockFactory ofStatus(String status) {
        this.paymentEvent.setStatus(status);
        return this;
    }

    public PaymentEventMockFactory ofDescription(String description) {
        this.paymentEvent.setDescription(description);
        return this;
    }
}
