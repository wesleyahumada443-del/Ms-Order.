package ms.order.service.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ms.order.service.domain.model.OrderOut;
import ms.order.service.domain.port.in.SendEventsInPort;
import ms.order.service.domain.port.out.PaymentOrderOutPort;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for the implement de business logic.
 *
 * @author Williams Ahumada
 * @author Wesley Ahumada
 * @author Jhonnatan Mendez
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class SendOrderUseCase implements SendEventsInPort {

    private final PaymentOrderOutPort orderEventsOutPort;

    public void send(OrderOut order) {
        log.info("Sending order: {}", order);
        orderEventsOutPort.sendMessage(order);
        log.info("Order send: {}", order);
    }
}
