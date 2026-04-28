package ms.order.service.infrastructure.adapter.repository;

import ms.order.service.domain.exceptions.RecordNotFoundException;
import ms.order.service.domain.model.OrderItem;
import ms.order.service.domain.port.out.OrderItemRepositoryOutPort;
import ms.order.service.infrastructure.adapter.mapper.OrderItemMapper;
import ms.order.service.infrastructure.persistence.entity.OrderItemEntity;
import ms.order.service.infrastructure.persistence.repository.OrderItemRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Component
public class OrderItemRepositoryAdapter extends BaseRepositoryAdapter<
        OrderItemRepository,
        OrderItemMapper,
        OrderItem,
        OrderItemEntity,
        UUID> implements OrderItemRepositoryOutPort {

    @Autowired
    public OrderItemRepositoryAdapter(OrderItemRepository repository, OrderItemMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public List<OrderItem> findByIds(List<UUID> ids) {
        var result = this.getRepository().findAllById(ids).stream().toList();

        if (result.isEmpty()) {
            return null;
        } else {
            return result.stream()
                    .map(this.getMapper()::toDomain)
                    .toList();
        }
    }

    @Override
    public OrderItem findById(UUID id) {
        var result = this.getRepository().findById(id).stream().toList();

        if (result.isEmpty()) {
            return null;
        } else {
            return this.getMapper().toDomain(result.get(0));
        }
    }

    @Override
    public List<OrderItem> findAll(Pageable pageable) {
        var page = this.getRepository().findAll(pageable);
        return Optional.of(page.getContent())
                .filter(ObjectUtils::isNotEmpty)
                .map(getMapper()::toDomain)
                .orElseThrow(() -> new RecordNotFoundException("Don't have any records "));
    }

    @Override
    public void deleteById(UUID id) {
        var result = getRepository().findById(id).stream().toList();
        if (!result.isEmpty()) {
            getRepository().deleteById(result.get(0).getId());
        } else {
            throw new RecordNotFoundException("No example found by id " + id);//TODO
        }
    }

}
