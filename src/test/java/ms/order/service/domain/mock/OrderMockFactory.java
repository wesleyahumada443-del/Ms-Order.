package ms.order.service.domain.mock;

import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.OrderItem;
import ms.order.service.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class OrderMockFactory {

    private Order order;

    public OrderMockFactory() {
        getOrder();
    }

    public OrderMockFactory getOrder() {
        this.order = Order.builder()
                .paymentMethodCustomerId(UUID.fromString("467972be-839d-474d-823e-90ef25cdd356"))
                .items(getOrderItem())
                .build();
        return this;
    }

    public List<OrderItem> getOrderItem() {
        return List.of(
                OrderItem.builder()
                        .product(Product.builder()
                                .id(UUID.fromString("4449f3a2-ea5c-411d-87dc-ab6d52ff5bee"))
                                .build())
                        .quantity(1)
                        .build(),

                OrderItem.builder()
                        .product(Product.builder()
                                .id(UUID.fromString("1cf53eb4-8853-49bc-8b39-a7132e1f1cc9"))
                                .build())
                        .quantity(2)
                        .build()
        );
    }

    public Order build() {
        return this.order;
    }


    public OrderMockFactory ofId(UUID id) {
        this.order.setId(id);
        return this;
    }

    public OrderMockFactory ofPaymentMethodCustomerId(UUID paymentMethodCustomerId) {
        this.order.setPaymentMethodCustomerId(paymentMethodCustomerId);
        return this;
    }

    public OrderMockFactory ofAmount(BigDecimal amount) {
        this.order.setAmount(amount);
        return this;
    }

    public OrderMockFactory ofStatus(OrderStatusEnum status) {
        this.order.setStatus(status);
        return this;
    }

    public OrderMockFactory ofItems(List<OrderItem> items) {
        this.order.setItems(items);
        return this;
    }
}
