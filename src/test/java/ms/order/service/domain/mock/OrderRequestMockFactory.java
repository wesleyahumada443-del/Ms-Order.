package ms.order.service.domain.mock;

import ms.order.service.domain.enums.OrderStatusEnum;
import ms.order.service.infrastructure.dto.http.request.OrderItemRequest;
import ms.order.service.infrastructure.dto.http.request.OrderRequest;

import java.util.List;
import java.util.UUID;


public class OrderRequestMockFactory {

    private OrderRequest orderRequest;

    public OrderRequestMockFactory() {
        getOrderRequest();
    }

    public OrderRequestMockFactory getOrderRequest() {
        this.orderRequest = OrderRequest.builder()
                .paymentMethodCustomerId(UUID.fromString("467972be-839d-474d-823e-90ef25cdd356"))
                .items(getOrderItemRequests())
                .build();
        return this;
    }

    public List<OrderItemRequest> getOrderItemRequests() {
        return (List.of(
                OrderItemRequest.builder()
                        .productId(UUID.fromString("4449f3a2-ea5c-411d-87dc-ab6d52ff5bee"))
                        .quantity(1)
                        .build(),
                OrderItemRequest.builder()
                        .productId(UUID.fromString("1cf53eb4-8853-49bc-8b39-a7132e1f1cc9"))
                        .quantity(2)
                        .build()
        ));
    }

    public OrderRequestMockFactory ofItems(List<OrderItemRequest> items) {
        this.orderRequest.setItems(items);
        return this;
    }

    public OrderRequestMockFactory ofIStatus(OrderStatusEnum statusEnum) {
        this.orderRequest.setStatus(statusEnum);
        return this;
    }

    public OrderRequestMockFactory ofPaymentMethod(UUID paymentMethod) {
        this.orderRequest.setPaymentMethodCustomerId(paymentMethod);
        return this;
    }

    public OrderRequest build() {
        return this.orderRequest;
    }

}