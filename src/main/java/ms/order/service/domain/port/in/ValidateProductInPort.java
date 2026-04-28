package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ValidateProductInPort {
    List<Product> validateByIds(Set<UUID> ids);
}
