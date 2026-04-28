package ms.order.service.domain.port.in;

import java.util.UUID;

public interface DeleteOrderInPort {
    void deleteById(UUID id);
}
