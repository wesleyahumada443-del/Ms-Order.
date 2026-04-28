package ms.order.service.infrastructure.adapter.mapper;

import ms.order.service.domain.mapper.BaseMapper;
import ms.order.service.domain.model.Order;
import ms.order.service.domain.model.Product;
import ms.order.service.infrastructure.dto.http.request.OrderRequest;
import ms.order.service.infrastructure.dto.http.response.OrderResponse;
import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import ms.order.service.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ProductMapper implements BaseMapper<Product, ProductEntity> {//TODO esta clase para que es?

    public abstract Order requestToDomain(OrderRequest orderRequest);
    public abstract OrderEntity requestToEntity(OrderRequest orderRequest);
    public abstract OrderResponse domainToResponse(Order order);
}
