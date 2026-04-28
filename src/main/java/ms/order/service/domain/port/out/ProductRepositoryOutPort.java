package ms.order.service.domain.port.out;

import ms.order.service.domain.model.Product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProductRepositoryOutPort extends BaseCRUDRepositoryOutPort<Product> {
    List<Product> findAllByIds(Set<UUID> ids);

}
