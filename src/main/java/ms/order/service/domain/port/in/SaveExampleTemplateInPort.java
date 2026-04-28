package ms.order.service.domain.port.in;

import ms.order.service.domain.model.Order;

public interface SaveExampleTemplateInPort {
    Order save(Order exampleObject);
}
