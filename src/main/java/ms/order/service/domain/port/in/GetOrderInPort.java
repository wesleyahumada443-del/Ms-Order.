package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Order;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GetOrderInPort {
    List<Order> getPaginated(Pageable pageable);
    Order getById(UUID id);
}
