package ms.order.service.infrastructure.adapter.repository;

import ms.order.service.domain.model.Order;
import ms.order.service.domain.port.out.OrderRepositoryOutPort;
import ms.order.service.infrastructure.adapter.mapper.OrderMapper;
import ms.order.service.infrastructure.persistence.entity.OrderEntity;
import ms.order.service.infrastructure.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Component
public class OrderRepositoryAdapter extends BaseRepositoryAdapter<
        OrderRepository,
        OrderMapper,
        Order,
        OrderEntity,
        UUID> implements OrderRepositoryOutPort {

    @Autowired
    public OrderRepositoryAdapter(OrderRepository repo, OrderMapper mapper) {
        super(repo, mapper);
    }

    @Override
    public Order create(Order order) {
        var orderEntity = this.getMapper().toEntity(order);
/*        order.getItems().forEach(item -> {
         var product = productMapper.toDomain(productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RecordNotFoundException("Product not found")));
            item.setQuantity(item.getQuantity());
            item.setProduct(product);
            items.add(item);
        });

        order.setItems(items);*/
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
        return this.getMapper().toDomain(this.getRepository().save(orderEntity));
    }
}
