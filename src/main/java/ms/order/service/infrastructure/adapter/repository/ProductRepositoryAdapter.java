package ms.order.service.infrastructure.adapter.repository;

import ms.order.service.domain.model.Product;
import ms.order.service.domain.port.out.ProductRepositoryOutPort;
import ms.order.service.infrastructure.adapter.mapper.ProductMapper;
import ms.order.service.infrastructure.persistence.entity.ProductEntity;
import ms.order.service.infrastructure.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * This class adapter is responsible for implement basic repository CRUD with JPA.
 *
 * @author Williams Ahumada
 */
@Component
public class ProductRepositoryAdapter extends BaseRepositoryAdapter<
        ProductRepository,
        ProductMapper,
        Product,
        ProductEntity,
        UUID> implements ProductRepositoryOutPort {

    @Autowired
    public ProductRepositoryAdapter(ProductRepository repo, ProductMapper mapper) {
        super(repo, mapper);
    }

    public List<Product> findAllByIds(Set<UUID> ids) {
       return getMapper().toDomain(getRepository().findAllById(ids));
    }
}
