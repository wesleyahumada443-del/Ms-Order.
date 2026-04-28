package ms.order.service.domain.mock;

import ms.order.service.domain.model.OrderOut;

import java.math.BigDecimal;
import java.util.UUID;


public class OrderOutMockFactory {

    private OrderOut orderOut;

    public OrderOutMockFactory() {
        getOrderOut();
    }

    public OrderOutMockFactory getOrderOut() {
        this.orderOut = OrderOut.builder()
                .paymentMethodCustomerId(UUID.fromString("467972be-839d-474d-823e-90ef25cdd356"))
                .amount(BigDecimal.valueOf(0))
                .build();
        return this;
    }

    public OrderOut build() {
        return this.orderOut;
    }


    public OrderOutMockFactory ofId(UUID id) {
        this.orderOut.setId(id);
        return this;
    }

    public OrderOutMockFactory ofPaymentMethodCustomerId(UUID paymentMethodCustomerId) {
        this.orderOut.setPaymentMethodCustomerId(paymentMethodCustomerId);
        return this;
    }

    public OrderOutMockFactory ofAmount(BigDecimal amount) {
        this.orderOut.setAmount(amount);
        return this;
    }

}
