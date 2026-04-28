package ms.order.service.domain.mock;
import ms.order.service.domain.model.Payment;
import java.math.BigDecimal;

import java.util.UUID;


public class PaymentMockFactory {

    private Payment payment;

    public PaymentMockFactory() {
        getPayment();
    }

    public PaymentMockFactory getPayment() {
        this.payment = Payment.builder()
                .orderId(UUID.randomUUID())
                .description("TEST")
                .amount(BigDecimal.valueOf(1200))
                .status("PAID")
                .build();
        return this;
    }

    public Payment build() {
        return this.payment;
    }


    public PaymentMockFactory ofId(UUID id) {
        this.payment.setId(id);
        return this;
    }

    public PaymentMockFactory ofOrderId(UUID orderId) {
        this.payment.setOrderId(orderId);
        return this;
    }

    public PaymentMockFactory ofAmount(BigDecimal amount) {
        this.payment.setAmount(amount);
        return this;
    }

    public PaymentMockFactory ofStatus(String status) {
        this.payment.setStatus(status);
        return this;
    }

    public PaymentMockFactory ofDescription(String description) {
        this.payment.setDescription(description);
        return this;
    }
}
