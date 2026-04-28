package ms.order.service.infrastructure.adapter.mapper;

import ms.order.service.domain.model.OrderItem;
import ms.order.service.infrastructure.dto.http.request.OrderItemRequest;
import ms.order.service.infrastructure.persistence.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.infrastructure.dto.http.response.OrderResponse;
import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import ms.order.service.infrastructure.dto.http.request.OrderRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses ={PaymentMapper.class})
public abstract class
   OrderMapper implements BaseMapper<Order, OrderEntity> {
    private final ProductMapper productMapper = new ProductMapperImpl();
    private final OrderItemMapper orderItemMapper = new OrderItemMapperImpl();


//    @Mapping(target = "items", ignore = true)
//    public abstract OrderEntity toEntityWithoutItem(Order domain);

//    @Mapping(target = "items", /*source = "items",*/ expression = "java(orderItemsToEntity(domain))")
//    public abstract OrderEntity toEntity(Order domain);
    /* {
        if ( domain == null ) {
            return null;
        }


    }*/

    public List<OrderItemEntity> orderItemsToEntity(Order order) {
        return (List<OrderItemEntity>) order.getItems().stream().map(item -> {
            return OrderItemEntity.builder()
                    .order(toEntity(order))
                    .quantity(item.getQuantity())
                    .product(productMapper.toEntity(item.getProduct()))
                    .build();
//            orderItemEntity.setOrder(this);
//            items.add(item);
        }).toList();
    }

    /*protected List<OrderItemEntity> orderItemListToOrderItemEntityList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemEntity> list1 = new ArrayList<OrderItemEntity>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemEntity( orderItem ) );
        }

        return list1;
    }*/

    public abstract Order requestToDomain(OrderRequest orderRequest);

    OrderItem orderItemRequestToOrderItem(OrderItemRequest orderItemRequest) {
        return orderItemMapper.requestToDomain(orderItemRequest);
    }
    public abstract OrderEntity requestToEntity(OrderRequest orderRequest);

    public abstract OrderResponse domainToResponse(Order order);
    public abstract OrderRequest fromOrder(Order order);
}
