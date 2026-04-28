package ms.order.service.infrastructure.adapter.mapper;

import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.domain.model.OrderItem;
import ms.order.service.domain.model.Product;
import ms.order.service.infrastructure.dto.http.request.OrderItemRequest;
import ms.order.service.infrastructure.persistence.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class OrderItemMapper implements BaseMapper<OrderItem, OrderItemEntity> {
//    public abstract OrderItem requestToDomain(OrderItemRequest orderItemRequest);
    OrderItem requestToDomain(OrderItemRequest orderItemRequest) {
        return OrderItem.builder()
                .product(Product.builder().id(orderItemRequest.getProductId()).build())
                .quantity(orderItemRequest.getQuantity())
                .build();
    }
}
