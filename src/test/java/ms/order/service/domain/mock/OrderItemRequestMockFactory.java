package ms.order.service.domain.mock;

import ms.order.service.infrastructure.dto.http.request.OrderItemRequest;

import java.util.UUID;


public class  OrderItemRequestMockFactory {

    private OrderItemRequest orderItemRequest;

    public OrderItemRequestMockFactory() {
        getOrderItemRequest();
    }

    public OrderItemRequestMockFactory getOrderItemRequest() {
        this.orderItemRequest = OrderItemRequest.builder()
                .productId(UUID.fromString("4449f3a2-ea5c-411d-87dc-ab6d52ff5bee"))
                .quantity(1)
                .build();
        return this;
    }


    public OrderItemRequestMockFactory ofIProductId(UUID productId) {
        this.orderItemRequest.setProductId(productId);
        return this;
    }

    public OrderItemRequestMockFactory ofQuantity(Integer quantity) {
        this.orderItemRequest.setQuantity(quantity);
        return this;
    }


    public OrderItemRequest build() {
        return this.orderItemRequest;
    }

}