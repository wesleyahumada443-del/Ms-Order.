package ms.order.service.domain.mock;

import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import ms.order.service.infrastructure.persistence.entity.OrderItemEntity;
import ms.order.service.infrastructure.persistence.entity.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEntityMockFactory {

    private OrderEntity orderEntity;

    public OrderEntityMockFactory() {
        buildDefaultOrderEntity();
    }

    private OrderEntityMockFactory buildDefaultOrderEntity() {
        this.orderEntity = OrderEntity.builder()
                .id(UUID.fromString("467972be-839d-474d-823e-90ef25cdd356"))
                .paymentMethodCustomerId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(300))
                .status(OrderStatusEnum.PENDING)
                .createdDate(LocalDateTime.now())
                .items(buildDefaultItems())
                .build();

        this.orderEntity.getItems().forEach(item -> item.setOrder(this.orderEntity));
        return this;
    }

    private List<OrderItemEntity> buildDefaultItems() {
        return List.of(
                OrderItemEntity.builder()
                        .product(ProductEntity.builder()
                                .id(UUID.fromString("4449f3a2-ea5c-411d-87dc-ab6d52ff5bee"))
                                .build())
                        .quantity(1)
                        .build(),
                OrderItemEntity.builder()
                        .product(ProductEntity.builder()
                                .id(UUID.fromString("1cf53eb4-8853-49bc-8b39-a7132e1f1cc9"))
                                .build())
                        .quantity(2)
                        .build()
        );
    }

    public OrderEntity build() {
        return this.orderEntity;
    }

    public OrderEntityMockFactory ofId(UUID id) {
        this.orderEntity.setId(id);
        return this;
    }

    public OrderEntityMockFactory ofPaymentMethodCustomerId(UUID paymentMethodCustomerId) {
        this.orderEntity.setPaymentMethodCustomerId(paymentMethodCustomerId);
        return this;
    }

    public OrderEntityMockFactory ofAmount(BigDecimal amount) {
        this.orderEntity.setAmount(amount);
        return this;
    }

    public OrderEntityMockFactory ofStatus(OrderStatusEnum status) {
        this.orderEntity.setStatus(status);
        return this;
    }

    public OrderEntityMockFactory ofItems(List<OrderItemEntity> items) {
        items.forEach(item -> item.setOrder(this.orderEntity));
        this.orderEntity.setItems(items);
        return this;
    }
}