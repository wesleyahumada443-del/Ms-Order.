package ms.order.service.domain.mock;

import ms.order.service.domain.model.OrderItem;
import ms.order.service.domain.model.Product;

import java.util.UUID;


public class OrderItemMockFactory {

    private OrderItem orderItem;

    public OrderItemMockFactory() {
        getOrderItem();
    }

    public OrderItemMockFactory getOrderItem() {
        this.orderItem = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(new ProductMockFactory().getProduct().build())
                .quantity(2)
                .build();
        return this;
    }

    public OrderItem build() {
        return this.orderItem;
    }


    public OrderItemMockFactory ofId(UUID id) {
        this.orderItem.setId(id);
        return this;
    }

    public OrderItemMockFactory ofProduct(Product product) {
        this.orderItem.setProduct(product);
        return this;
    }

    public OrderItemMockFactory ofQuantity(Integer quantity) {
        this.orderItem.setQuantity(quantity);
        return this;
    }

}